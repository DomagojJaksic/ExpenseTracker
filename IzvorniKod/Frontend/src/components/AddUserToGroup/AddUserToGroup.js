import React from "react";
import {Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input, Row, Col, Table} from 'reactstrap';
import InvitedUser from "../InvitedUser/InvitedUser";
import Header from "../Header/Header";
import "./AddUserToGroup.css"

class AddUserToGroup extends React.Component {

    state = {
        alreadyInvitedUsers: [],
        usernameInput: '',
        message: ''
    };

    constructor(props) {
        super(props);
    };

    handleChange = (event) => {
      this.setState({
          [event.target.name]: event.target.value
      });
    };

    cancelInvitation = (username) => {
        const body = {
            id: this.props.location.state.id,
            username: username
        };
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/groupmembers/', options)
            .then(response => {
                if (response.status === 401 || response.status === 400) {
                    this.setState({message: 'Neuspješno brisanje pozivnice.'});
                } else {
                    this.setState({ alreadyInvitedUsers: this.state.alreadyInvitedUsers
                            .filter(user => user.username !== username)});
                    this.setState({message: 'Uspješno brisanje pozivnice.'})
                }
            })
    };

    inviteUser = (username) => {
        const body = {
            id: this.props.location.state.id,
            username: username
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/groupmembers/', options)
            .then(response => {
                if (!response.ok) {
                    this.setState({message: 'Pozivanje korisnika neuspješno.'});
                } else {
                    this.getInvitedUsers()
                    this.setState({message: 'Uspješno pozivanje korisnika.'})
                }
            })
    };

    getInvitedUsers() {
        fetch('api/groups/' + this.props.location.state.id + '/invited')
            .then(response => response.json())
            .then(alreadyInvitedUsers => this.setState({ alreadyInvitedUsers }));
    }

    componentDidMount() {
        this.getInvitedUsers()
    }

    render() {
      let invitedUsers = this.state.alreadyInvitedUsers.map(user => {
          return (
                  <InvitedUser key={user.username} cancelInvitation={this.cancelInvitation.bind(this)} user={user}/>
          )
      });
    return (
        <div className={'addUserToGroup'}>
            <Header/>
            <Col sm="12" md={{ size: 5, offset: 3 }}>
                <Form className={"addUserG"}>
                    <Row>
                        <Col sm="12" md={{ size: 8, offset: 2 }}>

                            <FormGroup>
                                <Label for="addUserToGroupInstructions"><h5>Dodajte novog korisnika u grupu</h5></Label>
                            </FormGroup>

                            <FormGroup>
                                <Label for="member">Ime korisnika:</Label>
                                <Input name="usernameInput"
                                       placeholder="ime korisnika"
                                       onChange={this.handleChange}
                                       value={this.state.usernameInput}
                                />
                            </FormGroup>


                            <Col sm="12" md={{ size: 8, offset: 3 }}>
                                <Button color="secondary"
                                        onClick={() => this.inviteUser(this.state.usernameInput)}>POTVRDI
                                </Button>
                            </Col>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm="12" md={{ size: 8, offset: 2 }}>
                            <Col sm="12" md={{ size: 10, offset: 2 }}>
                                <FormGroup>
                                    <Label for="message">{this.state.message}</Label>
                                </FormGroup>
                            </Col>
                            <Col sm="12" md={{ size: 8, offset: 3 }}>
                                <FormGroup>
                                    <Label for="members"><h6>Pozvani korisnici: </h6></Label>
                                </FormGroup>
                            </Col>

                            <Col sm="20" md={{size: 20, offset: 0}}>
                                <Table className={"tab2"}
                                       bordered hover
                                >
                                    <thead>
                                        <tr>
                                            <th className={'overflow-auto text-center'} width='60%'>Korisnik</th>
                                            <th className={'overflow-auto text-center'} width='40%'>Poništi</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {invitedUsers}
                                    </tbody>
                                </Table>
                            </Col>
                        </Col>
                    </Row>

                </Form>
            </Col>
        </div>

    );
    }
}

export default AddUserToGroup;
