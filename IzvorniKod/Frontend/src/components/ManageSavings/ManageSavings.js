import React, { Component } from 'react';
import {Container, Row, Col, ListGroup, ListGroupItem, Table} from 'reactstrap';
import { Button, Form, FormGroup, Label, Input, FormText, InputGroup, InputGroupAddon } from 'reactstrap';
import './ManageSavings.css';
import InvitedUser from "../InvitedUser/InvitedUser";
import Header from "../Header/Header";


class ManageSavings extends Component {

    state = {
    	name: '',
    	description: '',
    	endDate: '',
    	targetedAmount: '',
    	username: '',
    	users: [],
        usernameInput: '',
        saving: {
    	    savingID: -1,
            name: '',
            endDate: '',
            targetedAmount: '',
            startDate: '',
            currentBalance: 0,
            creationTime: ''
        },
        isEdit: false,
        error: '',

   	};

    constructor(props) {
        super(props);
     }


  	handleChange = (event) => {
		this.setState({
			[event.target.name]: event.target.value
		});
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
        fetch('api/savingmembers/add', options)
            .then(response => {
                if (response.status === 401 || response.status === 400) {
                    this.setState({error: 'Pozivanje korisnika neuspješno.'});
                }
            })
    };

    sendData = () => {
        if(this.props.location.state.isEdit) {
            this.updateSaving();
        } else {
            this.createSaving();
        }
    };

    updateSaving = () => {
        const body = {
            savingID: this.state.saving.savingID,
            name: this.state.name,
            endDate: this.state.endDate,
            targetedAmount: this.state.targetedAmount,
            startDate: this.state.saving.startDate,
            creationTime: this.state.saving.creationTime,
            currentBalance: this.state.saving.currentBalance
        };
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/savings/' + this.state.saving.savingID, options)
            .then(response => {
                const statusCode = response.status;
                const data = response.json();
                return Promise.all([statusCode, data]);
            })
            .then(([statusCode, data]) => {
                if (statusCode >= 400 && statusCode < 600) {
                    this.setState({error: 'Uređivanje štednje neuspješno.'});
                } else {
                    this.setState({error: 'Uređivanje štednje uspješno.'});
                    this.redirectToSavings(data.savingID)
                }
            })
    };

    redirectToSavings = (savingID) => {
        this.props.history.push({
            pathname: '/savings',
            state:{
                savingID:savingID

            }
        })
    };

    createSaving = () => {
        const body = {
            name: this.state.name,
            endDate: this.state.endDate,
            targetedAmount: this.state.targetedAmount
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };

        fetch('api/savings/', options)
            // .then(response => response.json())
            .then(response => {
                const statusCode = response.status;
                const data = response.json();
                return Promise.all([statusCode, data]);
            })
            .then(([statusCode, data]) => {
                if(statusCode >= 400 && statusCode < 600) {
                    this.setState({error: 'Dodavanje štednje neuspješno.'})
                } else {
                    this.setState({error: ''});
                    this.setState({saving: data});
                    this.addUsersToSaving(data.savingID);
                    this.redirectToSavings(data.savingID)
                }
            })

    };


    addUsersToSaving = (id) => {
        for (let i = 0; i < this.state.users.length; i++) {
            this.inviteUser(id, this.state.users[i].username);
        }
    };

    addUser = (username) => {
        if(this.props.location.state.isEdit) {
            this.inviteUser(this.state.id, username);
            this.fetchInvitedUsers(this.state.id);
        } else {
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
        }

    };

    cancelInvitationForNewSaving = (username) => {
        this.setState({ users: this.state.users
                .filter(user => user.username !== username)});
    };

    cancelInvitationForEditedSaving = (username) => {
        const body = {
            id: this.state.id,
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
                    this.fetchInvitedUsers(this.state.id);
                    this.setState({ message: "Uspješno brisanje pozivnice."})
                }
            })
    };


    fetchInvitedUsers = (id) => {
        fetch('api/savings/' + id + '/invited')
            .then(response => response.json())
            .then(users => this.setState({ users }));
    };

    componentDidMount() {
        if(this.props.location.state.isEdit) {
            fetch('api/savings/' + this.props.location.state.id)
                .then(response => response.json())
                .then(response => {
                    if (response.status === 401 || response.status === 400) {
                        this.setState({ message: 'Neuspješno dohvaćanje štednje.' });
                    } else {
                        this.setState({saving: response});
                        this.setState({name: response.name});
                        this.setState({endDate: response.endDate});
                        this.setState({targetedAmount: response.targetedAmount});
                        this.fetchInvitedUsers(response.savingID);
                    }
                })
        }
    }

    componentDidCatch(error, info) {
        this.setState({error: 'Dodavanje štednje neuspješno.'});
    }

    render() {
        let invitedUsers;
        if(this.props.location.state.isEdit) {
            invitedUsers = this.state.users.map(user => {
                return (
                    <ListGroupItem>
                        <InvitedUser key={user.username}
                                     cancelInvitation={this.cancelInvitationForEditedSaving.bind(this)}
                                     user={user} />
                    </ListGroupItem>
                )
            });
        } else {
            invitedUsers = this.state.users.map(user => {
                return (
                        <InvitedUser key={user.username}
                                     cancelInvitation={this.cancelInvitationForNewSaving.bind(this)}
                                     user={user}
                        />
                )
            });
        }

        return (

            <div className="ManageSavings">
            <Header />
                <Col sm="12" md={{ size: 5, offset: 3 }}>
                    <Form className={"manageS"} >

                        <Row className="SavingName">
                            <Col sm="12" md={{ size: 9, offset: 2 }}>


                                <FormGroup>
                                    <Label for="manageSavings"><h4>Unesite podatke o štednji</h4></Label>
                                </FormGroup>

                                 <FormGroup>
                                    <Label for="name">Naziv štednje:</Label>
                                    <Input type="text"
                                    name="name"
                                    id="name"
                                    placeholder="naziv štednje"
                                    onChange = {this.handleChange}
                                    value={this.state.name}
                                    />
                                </FormGroup>
                            </Col>
                        </Row>


                        <Row className="DateRow">
                            <Col sm="12" md={{ size: 9, offset: 2 }}>
                                <FormGroup>
                                    <Label for="date">Ciljani datum: </Label>
                                    <Input type="date"
                                           name="endDate"
                                           id="endDate"
                                           onChange={this.handleChange}
                                           value={this.state.endDate}
                                    />
                                </FormGroup>
                            </Col>
                        </Row>

                        <Row className="TargetedAmount">
                             <Col sm="12" md={{ size: 9, offset: 2 }}>
                                  <FormGroup>
                                       <Label for="targetedAmount">Iznos:</Label>
                                       <Input
                                            type="text"
                                            name="targetedAmount"
                                            id="targetedAmount"
                                            placeholder="0.00"
                                            onChange={this.handleChange}
                                            value={this.state.targetedAmount}
                                       />
                                   </FormGroup>
                             </Col>
                        </Row>

                        <Row className="AddMember">
                            <Col sm='12' md={{ size: 9, offset: 2 }}>

                                <FormGroup >
                                    <Label for="member">Dodaj člana štednje:</Label>
                                </FormGroup>

                                <InputGroup>

                                    <Input name="usernameInput"
                                         placeholder="ime korisnika"
                                         onChange={this.handleChange}
                                         value={this.state.usernameInput}
                                    />

                                    <Button color="secondary"
                                            onClick={() => this.addUser(this.state.usernameInput)}
                                    >
                                        Dodaj
                                    </Button>
                                </InputGroup>
                            </Col>
                        </Row>


                        <Row>
                            <Col sm="12" md={{ size: 9, offset: 2 }}>

                                <Row>
                                    <Col sm="12" md={{ size: 9, offset: 2 }}>
                                        <Label>{this.state.error}</Label>
                                    </Col>
                                </Row>

                                <Row>
                                    <Col sm="20" md={{ size: 9, offset: 3 }}>
                                            <Button color="secondary"
                                                    onClick={() => this.sendData()}
                                            >POTVRDI
                                            </Button>
                                    </Col>
                                </Row>

                                <FormGroup className={"invitedUsersMS"}>
                                    <Col sm="20" md={{size: 15, offset: 3}}>
                                        <Label for="members"><h6>Pozvani korisnici: </h6></Label>
                                    </Col>
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
                                </FormGroup>
                            </Col>
                        </Row>
                    </Form>
                </Col>
            </div>
        );
    }

}

export default ManageSavings;