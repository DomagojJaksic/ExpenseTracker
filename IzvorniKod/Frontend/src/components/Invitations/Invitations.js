import React from "react";
import {Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input, Row, Col, Table, Container} from 'reactstrap';
import Invitation from "../Invitation/Invitation";
import Header from "../Header/Header";
import './Invitations.css';

class Invitations extends React.Component {

    state = {
        savings: [],
        groups: [],
        user: {
            userID: -1
        },
        savingMember: {
            savingMemberID: -1,
            saving: {
                savingID: -1,
                name: ''
            },
            user: {
                userID: -1
            },
            memberRole: ''
        },
        groupMember: {
            groupMemberID: -1,
            homeGroup: {
                groupID: -1,
                groupName: ''
            },
            user: {
                userID: -1
            },
            memberRole: ''
        },
        groupsMessage: '',
        savingsMessage: '',
        homeGroupMember: false
    };

    declineSavingInvitation = (id, username) => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch('api/savingmembers/' + username + "/" + id + '/decline', options)
            .then(response => {
                if (response.status === 401 || response.status === 400) {
                    this.setState({ message: 'Neuspješno odbijanje pozivnice.' });
                } else {
                    this.getSavingInvitations();
                    this.setState({ savings: this.state.savings
                            .filter(saving => saving.savingID !== id)});
                    this.setState({ message: "Uspješno odbijanje pozivnice."})
                }
            })
    };

    declineGroupInvitation = (id, username) => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch('api/groupmembers/' + username + "/" + id + '/decline', options)
            .then(response => {
                if (response.status === 401 || response.status === 400) {
                    this.setState({ message: 'Neuspješno odbijanje pozivnice.' });
                } else {
                    this.getGroupInvitations();
                    this.setState({ groups: this.state.groups
                            .filter(group => group.groupID !== id)});
                    this.setState({ message: "Uspješno odbijanje pozivnice."})
                }
            })
    };

    acceptSavingInvitation = (id, username) => {
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch('api/savingmembers/' + username + "/" + id + '/accept', options)
            .then(response => {
                if (!response.ok) {
                    this.setState({ message: 'Neuspješno prihvaćanje pozivnice.' });
                } else {
                    this.getSavingInvitations();
                    this.setState({ savings: this.state.savings
                            .filter(saving => saving.savingID !== id)});
                    this.redirectToSavings(id);
                    this.setState({ message: "Uspješno prihvaćanje pozivnice."})
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

    acceptGroupInvitation = (id, username) => {
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch('api/groupmembers/' + username + "/" + id + '/accept', options)
            .then(response => {
                if (response.status === 401 || response.status === 400) {
                    this.setState({ message: 'Neuspješno prihvaćanje pozivnice.' });
                } else {
                    this.setState({groupMember: true});
                    this.getGroupInvitations();
                    this.setState({ groups: this.state.groups
                            .filter(group => group.groupID !== id)});
                    this.redirectToGroup();
                    this.setState({ message: "Uspješno prihvaćanje pozivnice."})
                }
            })
    };

    redirectToGroup = () => {
        this.props.history.push({
            pathname: '/groupSettings',
        })
    };


    componentDidMount() {
        fetch("api/users/current")
            .then(data => data.json())
            .then(user => this.setState({user: user}))
            .then(() => this.getUsersGroup())
            .then(() => this.getSavingInvitations())
            .then(() => this.getGroupInvitations())

    }

    getSavingInvitations =() => {
        fetch('api/users/' + this.state.user.userID + '/savingInvitations')
            .then(response => response.json())
            .then(savings => this.setState({ savings }))
            .then(() => {
                if(this.state.savings.length <= 0) {
                    this.setState({savingsMessage: 'Nemate nijednu pozivnicu u štednju!'});
                }
            })
    };

    getGroupInvitations =() => {
        fetch('api/users/' + this.state.user.userID + '/groupInvitations')
            .then(response => response.json())
            .then(groups => this.setState({ groups }))
            .then(() => this.getUsersGroup())
            .then(() => {
                if(this.state.groups.length <= 0 && !this.state.groupMember) {
                    this.setState({groupsMessage: 'Nemate nijednu pozivnicu u grupu!'});
                }
            })

    };

    getUsersGroup = () => {
        fetch('api/users/' + this.state.user.userID + "/group")
            .then(response => {
                if (response.status === 401 || response.status === 400) {
                    this.setState({message: ''});
                    this.setState({groupMember: false});
                } else {
                    this.setState({groupMember: true});
                    this.setState({groupsMessage: 'Već ste član grupe!'});
                }
            })
    };


    render() {
        let savingInvitationsRow = this.state.savings.map(saving => {
            return (
                    <Invitation key={saving.name}
                                declineInvitation={this.declineSavingInvitation.bind(this)}
                                acceptInvitation={this.acceptSavingInvitation.bind(this)}
                                saving={saving}
                                name={saving.name}
                                id={saving.savingID}
                                user={this.state.user}
                    />
            )
        });

        let groupInvitationsRow = this.state.groups.map(group => {
            return (
                <Invitation key={group.groupName}
                            declineInvitation={this.declineGroupInvitation.bind(this)}
                            acceptInvitation={this.acceptGroupInvitation.bind(this)}
                            group={group}
                            name={group.groupName}
                            id={group.groupID}
                            user={this.state.user}
                />
            )
        });

        return (
            <div className={'Invitations'}>
            <Header/>
                <Col sm="12" md={{ size: 8, offset: 2 }}>
                    <Row>
                        <Container className={'container1'}>
                            <Row>
                                <Col xl="auto">
                                    <h2>Pozivnice u štednju</h2>
                                </Col>

                            </Row>
                            <Row>
                                <Col>
                                    <Table className={"tab2"}
                                           bordered hover
                                    >
                                        <thead>
                                            <tr>
                                                <th className={'text-center overflow-auto'} width='50%'>Ime štednje</th>
                                                <th className={'text-center overflow-auto'} width='25%'>Odbaci</th>
                                                <th className={'text-center overflow-auto'} width='25%'>Prihvati</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {savingInvitationsRow}
                                        </tbody>
                                    </Table>
                                </Col>
                            </Row>
                        </Container>
                    </Row>
                    <Row>
                        <Container className={'container1'}>
                            <Row>
                                <Col xl="auto">
                                    <h2>Pozivnice u grupu</h2>
                                </Col>

                            </Row>
                            <Row>
                                <Col>
                                    <Table className={"tab2"}
                                           bordered hover
                                    >
                                        <thead>
                                            <tr>
                                                <th className={'text-center overflow-auto'} width='50%'>Ime grupe</th>
                                                <th className={'text-center overflow-auto'} width='25%'>Odbaci</th>
                                                <th className={'text-center overflow-auto'} width='25%'>Prihvati</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {groupInvitationsRow}
                                        </tbody>
                                    </Table>
                                </Col>
                            </Row>
                        </Container>
                    </Row>
                </Col>
            </div>
        )
    }
}

export default Invitations;