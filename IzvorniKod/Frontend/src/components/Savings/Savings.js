import React, { Component } from 'react';
import {Button, Col, Container, Form, FormGroup, Input, Label, Row} from "reactstrap";
import Transactions from "../Transactions/Transactions";
import GroupMembersList from "../GroupMembersList/GroupMembersList";
import './Savings.css';
import Header from "../Header/Header";
import dateFormat from 'dateformat'


class Savings extends Component {

    state = {
        currentBalance: 0.,
        targetedAmount: 0.,
        startDate: '',
        endDate: '',
        saving: {
            savingID: -1,
            currentBalance: 0,
            targetedAmount: 0,
            startDate: '',
            endDate: '',
            name: ''
        },
        members:[],
        transactions: [],
        user: {
            userID: -1,
            username: ''
        },
        isAdmin: false,
        loadwhite: true,
        info: ''
    };

    componentDidMount() {
        fetch('api/users/current')
            .then(response => response.json())
            .then(response => {
                this.setState({user: response});
                this.fetchSaving(this.props.location.state.savingID);

            })

    }

    updateFields = () => {
        this.setState({currentBalance: this.state.saving.currentBalance});
        this.setState({endDate: this.state.saving.endDate});
        this.setState({targetedAmount: this.state.saving.targetedAmount});
        this.setState({startDate: this.state.saving.startDate});
    };

    fetchSaving = (id) => {
        fetch('api/savings/' + id)
            .then(response => response.json())
            .then(saving => {
                this.setState({saving: saving});
                this.updateFields();
                this.setInfo(id, this.state.user.userID);
            })
    };

    fetchSavingUpdate = (id) => {
        fetch('api/savings/' + id)
            .then(response => response.json())
            .then(saving => {
                this.setState({saving: saving});
                this.updateFields();
            })
    };

    editSaving = () => {
        this.props.history.push({
            pathname: '/manageSavings',
            state: { id: this.state.saving.savingID,
                isEdit: true
            }
        })
    };

    fetchMembers = (id) => {
        fetch('api/savings/' + id + '/users')
            .then(response => response.json())
            .then(response => this.setState({members: response}))
    };

    fetchTransactions = (id) => {
        fetch('api/savings/' + id + '/transactions')
            .then(response => response.json())
            .then(response => {
                response.sort(this.compareT);
                this.setState({transactions: response});
            })
    };

    compareT = (b, a) => {
        if (a.time < b.time)
            return -1;
        if (a.time > b.time)
            return 1;
        return 0;
    };

    checkIfUserIsAdmin = (id, userID) => {
        fetch('api/savings/' + id + '/admins')
            .then(response => response.json())
            .then(response => {
                const admins = response;
                for(let i = 0; i < admins.length; i++) {
                    if(admins[i].userID === userID) {
                        this.setState({isAdmin: true});
                        break;
                    } else {
                        this.setState({isAdmin: false})
                    }
                }
            })
    };

    setInfo = (id, userID) => {
        this.fetchMembers(id);
        this.fetchSavingUpdate(id);
        this.fetchTransactions(id);
        this.checkIfUserIsAdmin(id,userID);
        this.setState({loadwhite: false});
        this.setState({info: ""});
    };

    addNewUser = () => {
        this.props.history.push({
            pathname: '/addUserToSaving',
            state: { id: this.state.saving.savingID}
        })
    };

    navigateHP = () => {
        this.props.history.push('/homepage')
    };

    leaveSaving = () => {
        const body = {
            id: this.state.saving.savingID,
            username: this.state.user.username
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
                if(response.ok) {
                    this.navigateHP();
                }
            })
    };

    deleteSaving = () => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch('api/savings/' + this.state.saving.savingID, options)
            .then(response => {
                if(response.ok) {
                    this.navigateHP();
                }
            })
    };

    addNewTransaction = () => {
        this.props.history.push({
            pathname: '/manageSavingTransactions',
            state: { id: this.state.user.userID,
                     isEdit: false,
                     savingID: this.state.saving.savingID}
        })
    };

    editTransaction = (savingTransactionID) => {
        fetch('api/transactions/' + savingTransactionID + '/editable')
            .then(response => {
                if(response.ok) {
                    this.props.history.push({
                        pathname: '/manageSavingTransactions',
                        state: { id: this.state.user.userID,
                            isEdit: true,
                            savingTransactionID: savingTransactionID,
                            savingID: this.state.saving.savingID}
                    })
                } else {
                    this.setState({info: 'Nije moguće urediti ovu transakciju štednje.'})
                }
            })


    };

    render() {

        const{currentBalance,targetedAmount, startDate, endDate}= this.state;
        if(this.state.loadwhite) {
            return (
                <div className="Savings">
                    <Header/>
                </div>
            )
        }
        return (
            <div className="Savings">
                <Header/>
                <Col sm="12" md={{ size: 12, offset: 2 }}>
                    <Row className="SavingsForm_">
                        <Row className="SavingsRow">
                            <Col className="SavingsCol" xl="3.5" >
                                <Label for="currentBalance"><h6>Trenutni iznos: {currentBalance} kn</h6></Label>
                            </Col>
                            <Col className="SavingsCol" xl="3.5">
                                <Label for="targetedBalance"><h6>Ciljani iznos: {targetedAmount} kn</h6></Label>
                            </Col>
                            <Col className="SavingsCol" xl="3.5">
                                <Label for="startingDate"><h6>Datum početka: {dateFormat(startDate,'dd.mm.yyyy.')} </h6></Label>
                            </Col>
                            <Col className="SavingsCol" xl="3.5">
                                <Label for="targetedDate"><h6>Ciljani datum: {dateFormat(endDate,'dd.mm.yyyy.')}</h6></Label>
                            </Col>
                        </Row>
                    </Row>
                </Col>


                <Row>
                     <Col>
                          <Transactions user={this.state.user}
                                        transactions={this.state.transactions}
                                        savingID={this.state.saving.savingID}
                                        setInfo={this.setInfo}
                                        addNewTransaction={this.addNewTransaction}
                                        editTransaction={this.editTransaction}
                                        info={this.state.info}
                          />
                     </Col>
                </Row>


                <Row>
                    <Col>
                        <GroupMembersList  members={this.state.members}
                                           isAdmin={this.state.isAdmin}
                                           groupID={-1}
                                           user={this.state.user}
                                           savingID={this.state.saving.savingID}
                                           navigateHP={this.navigateHP}
                                           addNewUser={this.addNewUser}
                                           setInfo={this.setInfo}
                        />
                    </Col>
                </Row>
                <div className={"savingButtons"}>
                    <Col sm="12" md={{ size: 12, offset: 1 }}>
                        <Row className={"form-inline"}>
                            <Col>
                                <Button   disabled={!this.state.isAdmin}
                                          onClick={this.editSaving}>
                                    Uredi
                                </Button>
                            </Col>
                            <Col>
                                <Button   onClick={this.leaveSaving}>
                                    Napusti štednju
                                </Button>
                            </Col>
                            <Col>
                                <Button   disabled={!this.state.isAdmin}
                                          onClick={this.deleteSaving}>
                                    Obriši štednju
                                </Button>
                            </Col>
                        </Row>
                    </Col>
                </div>
              </div>
        );
    }
}

export default Savings;