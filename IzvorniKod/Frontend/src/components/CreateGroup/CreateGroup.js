import React, { Component } from 'react';
import {Container, Row, Col, ListGroup, ListGroupItem, FormFeedback, Table} from 'reactstrap';
import { Button, Form, FormGroup, Label, Input, FormText, InputGroup, InputGroupAddon } from 'reactstrap';
import InvitedUser from "../InvitedUser/InvitedUser";
import Header from "../Header/Header";
import "./CreateGroup.css"

class CreateGroup extends Component {

    state = {
        name: '',
        users: [],
        user: {
            userID: -1
        },
        hasGroup: false,
        validName: true,
        error: '',
        usernameInput: '',
        info: ''
    };

    cancelInvitation = (username) => {
        this.setState({ users: this.state.users.filter(user => user.username !== username)});
    };

    addUser = (username) => {

        fetch('api/users/username/' + username)
            .then(response => response.json())
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({error: 'Korisnik s ovim korisničkim imenom ne postoji.'});
                } else {
                    this.state.users.push(response);
                    this.setState({users: this.state.users});
                }
            })


    };

    handleChangeGroupName = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });

        this.groupNameExists(event.target.value);

    };

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };


    componentDidMount() {
        fetch('api/users/current')
            .then(response => response.json())
            .then(response => {
                this.setState({user: response});
                this.checkIfUserHasGroup(response.userID)
            })
    }

    checkIfUserHasGroup = (id) => {
        fetch('api/users/' + id + '/group')
            .then(response => {
                if(response.status >= 400 && response.status < 600) {
                    this.setState({hasGroup: false});
                } else {
                    this.setState({hasGroup: true});
                }
            })
    };


    groupNameExists = (name) => {
        const body = {
            name: this.state.name,
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/groups/name', options)
            .then(response => {
                if(response.status >= 400 && response.status < 600) {
                    this.setState({validName: true});
                } else {
                    this.setState({validName: false});
                }
            })
    };

    createGroup = () => {
        let isValid = true;
            if (!/^[-áàíóúšđžčćéëöüñÄĞİŞȘØøğışÐÝÞðýþA-Za-z-']+$/i.test(this.state.name)) {
                isValid = false;
            }
        if(isValid) {
            const body = {
                groupName: this.state.name,
            };
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            };
            fetch('api/groups/', options)
                .then(response => {
                    const statusCode = response.status;
                    const data = response.json();
                    return Promise.all([statusCode, data]);
                })
                .then(([statusCode, data]) => {
                    if (statusCode >= 400 && statusCode < 600) {
                        this.setState({error: 'Dodavanje grupe neuspješno.'})
                    } else {
                        this.setState({error: ''});
                        this.addUsersToGroup(data.groupID);
                        this.props.history.push({
                            pathname: '/groupsettings'
                        })
                    }
                })
        } else {
            this.setState({error: 'Ime grupe sadrži nedozvoljene znakove.'})
        }

    };

    addUsersToGroup = (id) => {
        for (let i = 0; i < this.state.users.length; i++) {
            this.inviteUser(id, this.state.users[i].username);
        }
    };

    inviteUser = (id, username) => {
        const body = {
            id: id,
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
                if (response.status === 401 || response.status === 400) {
                    this.setState({error: 'Pozivanje korisnika neuspješno.'});
                }
            })
    };

    render() {
        let invitedUsers = this.state.users.map(user => {
            return (
                    <InvitedUser key={user.username}
                                 cancelInvitation={this.cancelInvitation.bind(this)}
                                 user={user}/>
            )
        });
        if (this.state.hasGroup) {
            return (
                <div className="ManageSavings">
                    <Header/>
                    <Col sm="12" md={{size: 6, offset: 3}}>
                        <Form>
                            <Row className="SavingName">
                                <Col sm="12" md={{size: 6, offset: 3}}>
                                    <FormGroup>
                                        <Label for="info">Već ste član grupe!</Label>
                                    </FormGroup>
                                </Col>
                            </Row>
                        </Form>
                    </Col>
                </div>
            )
        } else {
            return (

                <div className="CreateGroup">
                    <Header/>
                    <Col sm="12" md={{size: 4, offset: 4}}>
                        <Form className="createG">

                            <Col sm="12" md={{size: 8, offset: 1}}>
                                <FormGroup>
                                    <Label for="createGroup"><h5>Unesite podatke o grupi:</h5></Label>
                                </FormGroup>
                            </Col>
                            <Col sm="12" md={{size: 8, offset: 2}}>
                                <Row>
                                    <FormGroup>
                                        <Label for="name">Naziv grupe:</Label>
                                        <Input type="text"
                                               name="name"
                                               id="name"
                                               placeholder="naziv grupe"
                                               onChange={this.handleChangeGroupName}
                                               value={this.state.name}
                                               invalid={!this.state.validName}
                                               required
                                        />
                                        <FormFeedback invalid>{'Grupa s ovim imenom već postoji.'}</FormFeedback>
                                    </FormGroup>
                                </Row>
                                <Row>
                                    <FormGroup>
                                        <Label for="member">Dodaj člana grupe:</Label>
                                    </FormGroup>

                                    <InputGroup>

                                        <Input name="usernameInput"
                                               placeholder="ime korisnika"
                                               onChange={this.handleChange}
                                               value={this.state.usernameInput}
                                        />

                                        <Button color="secondary"
                                                onClick={() => this.addUser(this.state.usernameInput)}>
                                            Dodaj
                                        </Button>

                                    </InputGroup>
                                </Row>
                            </Col>



                            <Row>
                                <Col sm="12" md={{size: 8, offset: 2}}>

                                    <Row>
                                        <Col sm="12" md={{size: 9, offset: 2}}>
                                            <Label>{this.state.error}</Label>
                                        </Col>
                                    </Row>

                                    <Row>
                                        <Col sm="20" md={{size: 6, offset: 3}}>
                                            <Button color="secondary"
                                                    onClick={() => this.createGroup()}
                                            >POTVRDI
                                            </Button>
                                        </Col>
                                    </Row>

                                    <FormGroup className={"invitedUsers"}>
                                        <Col sm="20" md={{size: 15, offset: 3}}>
                                            <Label for="members"><h6>Pozvani korisnici: </h6></Label>
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
                                    </FormGroup>

                                </Col>
                            </Row>
                        </Form>
                    </Col>
                </div>
            );

        }
    }
}

export default CreateGroup;