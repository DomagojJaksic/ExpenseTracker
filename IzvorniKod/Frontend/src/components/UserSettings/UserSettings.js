import React, { Component } from 'react';
import PasswordChange from "../PasswordChange/PasswordChange";
import { Col, Container, Row} from "reactstrap";
import HomeGroupSettings from "../HomeGroupSettings/HomeGroupSettings";
import ExpenseCategories from "../ExpenseCategories/ExpenseCategories";
import RevenueCategories from "../RevenueCategories/RevenueCategories";
import RevenueCategoriesRow from "../RevenueCategoriesRow/RevenueCategoriesRow";
import Header from "../Header/Header";




class UserSettings extends Component {

    state  = {
        user: {
            userID: -1,
            username: '',
            password: '',
            email: '',
            emailVerified: true,
            verificationCode: '',
            firstName: '',
            lastName: '',
            currentBalance: 0,
            lastHomeGroupMembershipChangeTime: ''
        },
        password: '',
        isGroupMember: null,
        expenseCategories: [],
        revenueCategories: [],
        group: {
            groupID: -1
        },
        buttonText: '',
        loadwhite: true
    };

    componentDidMount() {
        this.fetchUser();
    }

    navigateHP = () => {
        this.props.history.push('/homepage')
    };


    navigateCG = () => {
        this.props.history.push('/createGroup')
    };

    buttonAction = () => {
        if(!this.state.isGroupMember) {
            this.navigateCG();
        } else {
            this.leaveGroup();
        }
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

    fetchUser = () => {
        fetch('api/users/current')
            .then(response => response.json())
            .then(response => {
                this.setState({user: response});
                this.checkGroupMembership(response.userID);
            })
    };

    checkGroupMembership = (id) => {
        fetch('api/users/' + id + '/group')
            .then(response => {
                const statusCode = response.status;
                const data = response.json();
                return Promise.all([statusCode, data])
            })
            .then(([statusCode, data]) => {
                this.setState({loadwhite: false});
                if(statusCode >= 400 && statusCode < 600) {
                    this.setState({isGroupMember: false});
                    this.setState({buttonText: 'STVORI'});

                } else {
                    this.setState({buttonText: 'NAPUSTI'});
                    this.setState({isGroupMember: true});
                    this.setState({group: data});
                }
            });
            this.updateComponent(id);
    };

    updateComponent = (id) => {
        this.fetchRevenueCategories(id);
        this.fetchExpenseCategories(id);
    };

    fetchRevenueCategories = (id) => {
        fetch('api/users/' + id + '/revenuecategories')
            .then(response => response.json())
            .then(response => this.setState({revenueCategories: response}))
    };

    fetchExpenseCategories = (id) => {
        fetch('api/users/' + id + '/expensecategories')
            .then(response => response.json())
            .then(response => {
                this.setState({expenseCategories: response})
            })
    };

    addNewRevenueCategory = () => {
        this.props.history.push({
            pathname: '/manageCategoryOfRevenues',
            state: { id: this.state.user.userID,
                isEdit: false}
        })
    };

    editRevenueCategory = (revenueCategoryID) => {
        this.props.history.push({
            pathname: '/manageCategoryOfRevenues',
            state: { id: this.state.user.userID,
                isEdit: true,
                revenueCategoryID: revenueCategoryID}
        })
    };

    addNewExpenseCategory = () => {
        this.props.history.push({
            pathname: '/manageCategoryOfExpenses',
            state: { id: this.state.user.userID,
                isEdit: false}
        })
    };

    editExpenseCategory = (expenseCategoryID) => {
        this.props.history.push({
            pathname: '/manageCategoryOfExpenses',
            state: { id: this.state.user.userID,
                    isEdit: true,
                       expenseCategoryID: expenseCategoryID}
        })
    };



    render() {
        if(this.state.loadwhite) {
            return (
                <div>
                </div>
            )
        }

        if(this.state.isGroupMember) {
            return (
                <div className="UserSettings">
                    <Header/>
                    <Container>
                        <Row >
                            <Col >
                                <PasswordChange  navigateHP={this.navigateHP}
                                                 user={this.state.user}/>
                            </Col>
                            <Col>
                                <HomeGroupSettings  buttonAction={this.buttonAction}
                                                    isGroupMember={this.state.isGroupMember}
                                                    buttonText={this.state.buttonText}
                                />
                            </Col>
                        </Row>
                    </Container>


                </div>
            );
        }

        return (
            <div className="UserSettings">
            <Header/>
                <Container>
                    <Row >
                        <Col >
                            <ExpenseCategories expenseCategories={this.state.expenseCategories}
                                               isAdmin={true}
                                               addNewExpenseCategory={this.addNewExpenseCategory}
                                               editExpenseCategory={this.editExpenseCategory}
                                               updateComponent={this.updateComponent}
                                               groupID={-1}
                                               user={this.state.user}
                            />
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <RevenueCategories revenueCategories={this.state.revenueCategories}
                                               isAdmin={true}
                                               addNewRevenueCategory={this.addNewRevenueCategory}
                                               editRevenueCategory={this.editRevenueCategory}
                                               updateComponent={this.updateComponent}
                                               groupID={-1}
                                               user={this.state.user}
                            />
                        </Col>
                    </Row>
                    <Row >
                        <Col >
                            <PasswordChange  navigateHP={this.navigateHP}
                                                user={this.state.user}/>
                        </Col>
                        <Col>
                            <HomeGroupSettings  buttonAction={this.buttonAction}
                                                isGroupMember={this.state.isGroupMember}
                                                buttonText={this.state.buttonText}
                            />
                        </Col>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default UserSettings;