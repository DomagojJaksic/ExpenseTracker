import React, { Component } from 'react';
import {Container, Row, Col, Input, Label} from 'reactstrap';
import SavingsList from "../SavingsList/SavingsList";
import NotificationList from "../NotificationList/NotificationList";
import ExpenseList from "../ExpenseList/ExpenseList";
import RevenueList from "../RevenueList/RevenueList";
import './HomePage.css';
import Header from "../Header/Header";


class HomePage extends Component {


    state = {
        user: {
            userID: -1,
            username: '',
            currentBalance: 0
        },
        homeGroup:'',
        isGroupMember:false,
        userID:'',
        username:'',
        currentBalance:'',
        groupName:'privatni korisnik',
        displayPeriod:'Cijelo vrijeme',
        revenues:[],
        expenses:[],
        savings:[],
        notifications:[],
        loadwhite: true

    };

    constructor(props) {
        super(props);

    }

    componentDidMount() {
        this.getUser()

    }

    getRevenues = (userID) => {
        const body = {
            timePeriod: this.state.displayPeriod
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/users/' + userID + '/revenues', options)
            .then(response => response.json())
            .then(revenues => {
                revenues.sort(this.compareT);
                this.setState({revenues : revenues});
            });

    };

    getExpenses = (userID) => {
        const body = {
            timePeriod: this.state.displayPeriod
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/users/' + userID + '/expenses', options)
            .then(response => response.json())
            .then(expenses => {
                expenses.sort(this.compareT);
                this.setState({expenses : expenses})
            });

    };

    getSavings = (userID) => {

        fetch('api/users/' + userID + '/savings')
            .then(response => response.json())
            .then(savings => {
                savings.sort(this.compareSavings);
                this.setState({savings : savings})
            });

    };

    getNotifications = (userID) => {

        fetch('api/users/' + userID + '/notifications')
            .then(response => response.json())
            .then(notifications => {
                notifications.sort(this.compareNotifs);
                this.setState({notifications : notifications});
            });

    };

    compareNotifs = (b, a) => {
        if (a.notificationTime < b.notificationTime)
            return -1;
        if (a.notificationTime > b.notificationTime)
            return 1;
        return 0;
    };

    compareT = (b, a) => {
        if (a.date < b.date)
            return -1;
        if (a.date > b.date)
            return 1;
        return 0;
    };

    compareSavings = (a, b) => {
        if (a.endDate < b.endDate)
            return -1;
        if (a.endDate > b.endDate)
            return 1;
        return 0;
    };

    addRevenue = (userID) => {
        this.props.history.push({
            pathname: '/manageRevenues',
            state: {
                id: userID,
                isEdit: false
            }
        })
    };


    editRevenue = (revenueID) => {
        this.props.history.push({
            pathname: '/manageRevenues',
            state: {
                revenueID: revenueID,
                isEdit: true
            }
        })
    };

    addExpense = (userID) => {
        this.props.history.push({
            pathname: '/manageExpenses',
            state: {
                id: userID,
                isEdit: false
            }
        })
    };

    editExpense = (expenseID) => {
        this.props.history.push({
            pathname: '/manageExpenses',
            state: {
                expenseID: expenseID,
                isEdit: true
            }
        })
    };

    addSaving = (userID) => {
        this.props.history.push({
            pathname: '/manageSavings',
            state: {
                id: userID,
                isEdit: false
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



    getUser = ()=> {
        fetch('api/users/current' )
            .then(response => response.json())
            .then(user => {
                this.setState({ user });
                this.getHomeGroup(user.userID, user.currentBalance);
                this.fetchData(user.userID);
            })

    };

    getUserUpdate = () => {
        fetch('api/users/current' )
            .then(response => response.json())
            .then(user => {
                this.setState({ user });
                this.getHomeGroup(user.userID, user.currentBalance);
                this.setState({loadwhite: false})
            })

    };

    fetchData = (id) => {
        this.getRevenues(id);
        this.getExpenses(id);
        this.getSavings(id);
        this.getNotifications(id);
        this.getUserUpdate()
    };



    getHomeGroup =(userID, currentBalance)=>{
        fetch('api/users/' + userID + '/group' )
            .then(response => {
                const statusCode = response.status;
                const data = response.json();
                return Promise.all([statusCode,data])
            })
            .then(([statusCode,data]) =>{
                if(statusCode>=400 && statusCode<600){
                        this.setState({isGroupMember:false});
                    this.setState({currentBalance: currentBalance});

                } else {
                    this.setState({isGroupMember: true});
                    this.setState(({groupName: data.groupName}));

                    this.setState({currentBalance: data.balance.toFixed(2)});
                }
        })
    };


    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    handleChangeDisplayPeriod = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });

        this.getUser(this.state.userID);
    };

    render() {

        const {currentBalance, groupName}=this.state;

        if(this.state.loadwhite) {
            return (
                <div className="HomePage">
                    <Header/>
                </div>
            )
        }

        return (
            <div className="HomePage">
                <Header/>
                <Container >
                    <Row className="HomePageRow">
                        <Col >
                            <Label><h5>Stanje računa: {currentBalance} kn</h5></Label>
                        </Col>
                        <Col>
                            <Label><h5>Grupa: {groupName}</h5></Label>
                        </Col>
                    </Row>
                    <Row className="HomePageRow">
                        <Col >
                            <Label><h5>Odaberite vremensko razdoblje prikaza: </h5></Label>
                        </Col>
                        <Col>
                            <Input  type="select"
                                    name="displayPeriod"
                                    onChange={this.handleChangeDisplayPeriod}
                                    value={this.state.displayPeriod}
                            >
                                <option value={'1 tjedan'}>posljednji tjedan</option>
                                <option value={'1 mjesec'}>posljednji mjesec</option>
                                <option value={'3 mjeseca'}>posljednja tri mjeseca</option>
                                <option value={'6 mjeseci'}>posljednjih šest mjeseci</option>
                                <option value={'1 godina'}>posljednjih godinu dana</option>
                                <option value={'Cijelo vrijeme'}>cijelo vrijeme</option>
                            </Input>
                        </Col>
                    </Row>
                    <Row>
                        <NotificationList userID={this.userID }
                                          notifications={this.state.notifications}
                                          displayPeriod={this.displayPeriod}
                                          fetchData={this.fetchData}
                        />
                    </Row>
                    <Row>
                        <RevenueList   revenues = {this.state.revenues}
                                      displayPeriod={this.displayPeriod}
                                       usernameCurrent = {this.state.user.username}
                                       fetchData={this.fetchData}
                                       user={this.state.user}
                                       addRevenue = {this.addRevenue}
                                       editRevenue = {this.editRevenue}
                        />
                    </Row>
                    <Row>
                        <ExpenseList expenses = {this.state.expenses}
                                     displayPeriod={this.displayPeriod}
                                     usernameCurrent = {this.state.user.username}
                                     fetchData={this.fetchData}
                                     user={this.state.user}
                                     addExpense = {this.addExpense}
                                     editExpense = {this.editExpense}
                        />
                    </Row>
                    <Row>
                        <SavingsList savings={this.state.savings}
                                     displayPeriod={this.displayPeriod}
                                     userID={this.userID }
                                     fetchData={this.fetchData}
                                     user={this.state.user}
                                     addSaving={this.addSaving}
                                     redirectToSavings={this.redirectToSavings}
                                     getSavings={this.getSavings}

                        />
                    </Row>
                </Container>
            </div>
        );
    }
}

export default HomePage;