import React, { Component } from 'react';
import GroupMembersList from "../GroupMembersList/GroupMembersList";
import {Button, Col, Container, Row, Label} from "reactstrap";
import ExpenseCategories from "../ExpenseCategories/ExpenseCategories";
import RevenueCategories from "../RevenueCategories/RevenueCategories";
import './GroupSettings.css';
import Header from "../Header/Header";

class GroupSettings extends Component {

    state = {
        user: {
            userID: -1,
            username: ''
        },
        isGroupMember: false,
        isAdmin: false,
        group: {
            groupID: -1,
            groupName: ''
        },
        revenueCategories: [],
        expenseCategories: [],
        members: [],
        loadwhite: true
    };

    componentDidMount() {
        fetch('api/users/current')
            .then(response => response.json())
            .then(response => {
                this.setState({user: response});
                this.checkGroupMembership(response.userID);
            });
    }

    checkGroupMembership = (id) => {
        fetch('api/users/' + id + '/group')
            .then(response => {
                const statusCode = response.status;
                const data = response.json();
                return Promise.all([statusCode, data])
            })
            .then(([statusCode, data]) => {
                if(statusCode >= 400 && statusCode < 600) {
                    this.setState({isGroupMember: false});
                    this.setState({loadwhite: false})
                } else {
                    this.setState({isGroupMember: true});
                    this.setState({loadwhite: false});
                    this.setState({group: data});
                    this.setInfo(data.groupID, id);
                }
            })
    };

    setInfo = (id, userID) => {
        this.fetchRevenueCategories(userID);
        this.fetchExpenseCategories(userID);
        this.fetchMembers(id);
        this.checkIfUserIsAdmin(id, userID);
    };

    fetchRevenueCategories = (id) => {
        fetch('api/users/' + id + '/revenuecategories')
            .then(response => response.json())
            .then(response => {
                this.setState({revenueCategories: response})
            })
    };

    fetchExpenseCategories = (id) => {
        fetch('api/users/' + id + '/expensecategories')
            .then(response => response.json())
            .then(response => {
                this.setState({expenseCategories: response})
            })
    };

    fetchMembers = (id) => {
        fetch('api/groups/' + id + '/users')
            .then(response => response.json())
            .then(response => {
                this.setState({members: response})
            })
    };

    checkIfUserIsAdmin = (id, userID) => {
        fetch('api/groups/' + id + '/admins')
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

    navigateHP = () => {
        this.props.history.push('/homepage')
    };

    addNewUser = () => {
        this.props.history.push({
            pathname: '/addUserToGroup',
            state: { id: this.state.group.groupID}
        })
    };

    addNewExpenseCategory = () => {
        this.props.history.push({
            pathname: '/manageCategoryOfExpenses',
            state: { id: this.state.group.groupID,
                    isEdit: false}
        })
    };

    editExpenseCategory = (expenseCategoryID) => {
        this.props.history.push({
            pathname: '/manageCategoryOfExpenses',
            state: { id: this.state.group.groupID,
                isEdit: true,
                expenseCategoryID: expenseCategoryID}
        })
    };

    addNewRevenueCategory = () => {
        this.props.history.push({
            pathname: '/manageCategoryOfRevenues',
            state: { id: this.state.group.groupID,
                isEdit: false}
        })
    };

    editRevenueCategory = (revenueCategoryID) => {
        this.props.history.push({
            pathname: '/manageCategoryOfRevenues',
            state: { id: this.state.group.groupID,
                isEdit: true,
                revenueCategoryID: revenueCategoryID}
        })
    };

    leaveGroup = () => {
        const body = {
            id: this.state.group.groupID,
            username: this.state.user.username
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
                if(response.ok) {
                    this.navigateHP();
                }
            })
    };

    deleteGroup = () => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/groups/' + this.state.group.groupID, options)
            .then(response => {
                if(response.ok) {
                    this.navigateHP();
                }
            })
    };

    render() {
        if(this.state.loadwhite) {
            return (
                <div className="GroupSettings">
                    <Header/>
                </div>
            )
        }
        if(!this.state.isGroupMember) {
            return (
                <div className="GroupSettings">
                    <Header/>
                    <Row>
                        <Col sm="12" md={{ size: 6, offset: 4 }}>
                            <Label className={"errorLabelGS"}><h3>Korisnik nije član grupe!</h3></Label>
                        </Col>
                    </Row>
                </div>
            )

        }
        return (
            <div className="GroupSettings">
                <Header/>
                <Container>
                    <Row className={'gmList'}>
                       <Col sm="12" md={{ size: 6, offset: 3 }}>
                       <GroupMembersList members={this.state.members}
                                         isAdmin={this.state.isAdmin}
                                         groupID={this.state.group.groupID}
                                         setInfo={this.setInfo}
                                         user={this.state.user}
                                         navigateHP={this.navigateHP}
                                         addNewUser={this.addNewUser}
                       />
                       </Col>
                    </Row>

                    <Row>
                        <Col>
                            <ExpenseCategories  addNewExpenseCategory={this.addNewExpenseCategory}
                                                editExpenseCategory={this.editExpenseCategory}
                                                expenseCategories={this.state.expenseCategories}
                                                setInfo={this.setInfo}
                                                groupID={this.state.group.groupID}
                                                user={this.state.user}
                                                isAdmin={this.state.isAdmin}

                            />
                            <RevenueCategories addNewRevenueCategory={this.addNewRevenueCategory}
                                               editRevenueCategory={this.editRevenueCategory}
                                               revenueCategories={this.state.revenueCategories}
                                               setInfo={this.setInfo}
                                               groupID={this.state.group.groupID}
                                               user={this.state.user}
                                               isAdmin={this.state.isAdmin}/>
                        </Col>
                    </Row>

                    <div className={'groupButtons'}>
                        <Col sm="12" md={{ size: 12, offset: 2 }}>
                            <Row className={'form-inline'}>
                                <Col>
                                    <Button className={'leaveB'}
                                            onClick={this.leaveGroup}
                                    >Napusti grupu
                                    </Button>
                                </Col>
                                <Col>
                                    <Button className={'deleteB'}
                                            disabled={!this.state.isAdmin}
                                            onClick={this.deleteGroup}
                                    >Obriši grupu
                                    </Button>
                                </Col>
                            </Row>
                        </Col>
                    </div>
                </Container>
            </div>
        );
    }
}

export default GroupSettings;