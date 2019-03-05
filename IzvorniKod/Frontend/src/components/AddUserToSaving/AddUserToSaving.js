import React, { Component } from "react";
import {Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input, Row, Col, Table} from 'reactstrap';
import InvitedUser from "../InvitedUser/InvitedUser";
import "./AddUserToSaving.css"
import PropTypes from "prop-types";
import { withRouter } from "react-router";
import Header from "../Header/Header";

class AddUserToSaving extends Component {
    static propTypes = {
        match: PropTypes.object.isRequired,
        location: PropTypes.object.isRequired,
        history: PropTypes.object.isRequired
    };


    state = {
        alreadyInvitedUsers: [],
        usernameInput: '',
        message: ''
    };

    constructor(props) {
        super(props);
        const { match, location, history } = this.props;
        this.setState({id: location.state.id});
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
        fetch('api/savingmembers/', options)
            .then(response => {
                if (response.status === 401 || response.status === 400) {
                    this.setState({ message: 'Neuspješno brisanje pozivnice.' });
                } else {
                    this.setState({ alreadyInvitedUsers: this.state.alreadyInvitedUsers
                            .filter(user => user.username !== username)});
                    this.setState({ message: "Uspješno brisanje pozivnice."})
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
        fetch('api/savingmembers/add', options)
            .then(response => {
                if (!response.ok) {
                    this.setState({message: 'Pozivanje korisnika neuspješno.'});
                } else {
                   this.getInvitedUsers();
                   this.setState({message: 'Uspješno pozivanje korisnika.'});
                }
            })
    };

    getInvitedUsers() {
        fetch('api/savings/' + this.props.location.state.id + '/invited')
            .then(response => response.json())
            .then(alreadyInvitedUsers => this.setState({ alreadyInvitedUsers }));
    }

    componentDidMount() {
        this.getInvitedUsers()
    }

    render() {
        let invitedUsers = this.state.alreadyInvitedUsers.map(user => {
          return (
                  <InvitedUser key={user.username}
                               cancelInvitation={this.cancelInvitation.bind(this)}
                               user={user} />
          )
        });
        return (
            <div className={'addUserToSaving'}>
                <Header/>
                <Col sm="12" md={{ size: 5, offset: 3 }}>
                    <Form className={"addUserS"}>
                        <Row>
                            <Col sm="12" md={{ size: 8, offset: 2 }}>

                                <FormGroup>
                                    <Label for="addUserToSavingInstructions"><h5>Dodajte novog korisnika u štednju</h5></Label>
                                </FormGroup>

                                <FormGroup>
                                    <Label for="member">Ime korisnika: </Label>
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
                                        <Label for="errorMessage">{this.state.message}</Label>
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

export default AddUserToSaving;
