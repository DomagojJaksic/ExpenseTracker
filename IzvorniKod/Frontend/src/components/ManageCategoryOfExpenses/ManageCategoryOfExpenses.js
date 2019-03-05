import React from "react";
import { Button, Form, FormGroup, Label, Input, FormText, Row, Col } from 'reactstrap';
import Header from "../Header/Header";
import "./ManageCategoryOfExpenses.css"


class ManageCategoryOfExpenses extends React.Component {
    state = {
        categoryName: '',
        timePeriod: '',
        limitPercentage: 100,
        user: {
            userID: -1
        },
        homeGroup: {
            groupID: -1
        },
        expenseCategory: { //test values
            expenseCategoryID: -1,
            name: '',
            timePeriod: '',
            limitPercentage: 0,
            user: null,
            homeGroup: null
        },
        isGroup: false,
        isEdit: false,
        message: ''
    };

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };


    updateFields = () => {
        this.setState({categoryName: this.state.expenseCategory.name});
        this.setState({timePeriod: this.state.expenseCategory.timePeriod});
        this.setState({limitPercentage: this.state.expenseCategory.limitPercentage});
    };


    addNewExpenseCategory = () => {

        const body = {
            name: this.state.categoryName,
            timePeriod: this.state.timePeriod,
            limitPercentage: this.state.limitPercentage,
            user: this.state.user,
            homeGroup: this.state.homeGroup
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/expensecategories/', options)
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({message: 'Dodavanje kategorije neuspješno.' });
                } else {
                    if(this.state.homeGroup === null) {
                        this.props.history.push('/settings')
                    } else {
                        this.props.history.push('/groupSettings')
                    }
                    this.setState({message: 'Uspješno dodana kategorija troškova.'});
                }
            })
    };

    editExpenseCategory = () => {
        const body = {
            expenseCategoryID: this.state.expenseCategory.expenseCategoryID,
            name: this.state.categoryName,
            timePeriod: this.state.timePeriod,
            limitPercentage: this.state.limitPercentage,
            user: this.state.user,
            homeGroup: this.state.homeGroup
        };
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/expensecategories/' + this.state.expenseCategory.expenseCategoryID, options)
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({message: 'Uređivanje kategorije neuspješno.' });
                } else {
                    if(this.state.homeGroup === null) {
                        this.props.history.push('/settings')
                    } else {
                        this.props.history.push('/groupSettings')
                    }
                    this.setState({message: 'Uspješno uređena kategorija troškova.'});
                }
            })
    };

    sendData = () => {
        if(this.props.location.state.isEdit) {
            this.editExpenseCategory(this.props.location.state.id);
        } else {
            this.addNewExpenseCategory(this.props.location.state.id);
        }
    };

    componentDidMount() {
        fetch("api/users/current")
            .then(data => data.json())
            .then(user => this.setState({user: user}))
            .then(() => this.checkIfUserHasGroup(this.state.user.userID));


        if(this.props.location.state.isEdit) {
            this.fetchExpenseCategory(this.props.location.state.expenseCategoryID);
            this.setState({isEdit: true});
            // this.updateFields();
        }
    }

    checkIfUserHasGroup = (userID) =>  {
        fetch("api/users/" + userID + "/group")
            .then(data => data.json())
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({homeGroup: null})
                } else {
                    this.setState({homeGroup: response});
                    this.setState({isGroup: true});
                    this.setState({user: null})
                }
            });
    };

    fetchExpenseCategory = (id) => {
        fetch("api/expensecategories/" + id)
            .then(response => response.json())
            .then(response => {
                this.setState({expenseCategory: response})
            })
            .then(() =>this.updateFields())
    };

    render() {
        return (
            <div className={'managecategoryofexpenses'}>
            <Header/>
            <Row>
                <Col sm="12" md={{ size: 5, offset: 3 }}>
                    <Form className={"mngEC"}>
                        <FormGroup>
                            <Label for="instruction"><h5>Unesite podatke o kategoriji troška: </h5></Label>
                        </FormGroup>

                        <FormGroup>
                            <Label for="expenseCategoryName">Ime kategorije troška: </Label>
                            <Input type="text"
                                   name="categoryName"
                                   id="categoryName"
                                   placeholder="Ime kategorije troška"
                                   onChange={this.handleChange}
                                   value={this.state.categoryName}
                            />
                        </FormGroup>

                        <FormGroup>
                            <Label for="instructionForNotif">
                                Uredite postavke notifikacija koje su vezane za kategoriju troška</Label>
                        </FormGroup>

                        <FormGroup>
                            <Label for="expenseLimit">Postotak prosječne potrošnje u zadanom vremenskom razdoblju
                                koji uzrokuje stvaranje obavijesti: </Label>
                            <Input
                                type="number"
                                name="limitPercentage"
                                id="limit"
                                placeholder="Postotak"
                                onChange={this.handleChange}
                                value={this.state.limitPercentage}
                            />
                        </FormGroup>

                        <FormGroup>
                            <Label for="timeLimitInstruction">Vremensko razdoblje na koje se odnose notifikacije</Label>
                            <Input  type="select"
                                    name="timePeriod"
                                    onChange={this.handleChange}
                                    value={this.state.timePeriod}
                            >
                                <option>Odaberite vremensko razdoblje</option>
                                <option value = "NO_NOTIFICATION">Bez obavijesti</option>
                                <option value = "THREE_MONTHS">3 mjeseca</option>
                                <option value = "SIX_MONTHS">6 mjeseci</option>
                                <option value = "ONE_YEAR">1 godina</option>
                            </Input>
                        </FormGroup>
                        <FormGroup>
                            <Label for="alert">{this.state.message}</Label>
                        </FormGroup>
                        <Button onClick={() => this.sendData()}>
                            POTVRDI
                        </Button>
                    </Form>
                </Col>
            </Row>
            </div>
        );
    }
}
export default ManageCategoryOfExpenses;
