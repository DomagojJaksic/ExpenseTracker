import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input } from 'reactstrap';
import './ManageExpenses.css';
import ExpenseCategory from '../ExpenseCategory/ExpenseCategory';
import Header from "../Header/Header";


class ManageExpenses extends Component {

	state = {
	    expenseCategories: [],
        expenseID: -1,
        user: null,
		date: '',
		description: '',
		amount: '',
        entryTime: '',
        expenseCategoryName: '',
		expenseCategory: null,
        info: '',
        // isEdit: !true
	};

    constructor(props) {
        super(props);
     }

  	isValid = () => {
        const {amount} = this.state;
        return amount > 0;
    };

    handleChange = (event) => {
		this.setState({
			[event.target.name]: event.target.value
		});
	};

    handleChangeExpenseCategory = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });

        for (let i = 0; i < this.state.expenseCategories.length; i++) {
            if(this.state.expenseCategories[i].name === event.target.value) {
                this.setState({expenseCategory: this.state.expenseCategories[i]});
            }
        }
    };

    sendData = () => {
        if(this.props.location.state.isEdit) {
            this.editExpense();
        } else {
            this.addExpense();
        }
    };

    addExpense = () => {
        const body = {
            user: this.state.user,
            description: this.state.description,
            date: this.state.date,
            amount: this.state.amount,
            expenseCategory: this.state.expenseCategory
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/expenses/', options)
            .then(response => {
                if(response.ok) {
                    this.props.history.push('/homepage');
                    this.setState({info: 'Trošak uspješno dodan.'})
                } else {
                    this.setState({info: 'Neuspješno dodavanje troška.'})
                }
            })
    };


    editExpense = () => {
        const body = {
            expenseID: this.state.expenseID,
            entryTime: this.state.entryTime,
            user: this.state.user,
            description: this.state.description,
            date: this.state.date,
            amount: this.state.amount,
            expenseCategory: this.state.expenseCategory
        };
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/expenses/' + this.state.expenseID, options)
            .then(response => {
                if(response.ok) {
                    this.props.history.push('/homepage');
                    this.setState({info: 'Uređivanje troška uspješno.'})
                } else {
                    this.setState({info: 'Neuspješno uređivanje troška.'})
                }
            })
    };

    getExpenseCategories = (id) => {
        fetch('api/users/' + id + '/expensecategories')
            .then(response => response.json())
            .then(expenseCategories => this.setState({ expenseCategories }));
    };

    fetchExpense = (id) => {
        fetch('api/expenses/' + id)
            .then(response => response.json())
            .then(expense => {
                this.setState({expenseID: expense.expenseID});
                this.setState({amount: expense.amount});
                this.setState({description: expense.description});
                this.setState({entryTime: expense.entryTime});
                this.setState({date: expense.date});
                this.setState({user: expense.user});
                this.setState({expenseCategory: expense.expenseCategory});
                this.setState({expenseCategoryName: expense.expenseCategory.name});
            });
    };

    componentDidMount() {
        if (this.props.location.state.isEdit) {
            this.setState({isEdit: true});
            this.fetchExpense(this.props.location.state.expenseID);
        }


        fetch('api/users/current')
            .then(response => response.json())
            .then(response => {
               this.setState({user: response});
               this.getExpenseCategories(response.userID);
            })

    }


    render() {
        let usersExpenseCategories = this.state.expenseCategories.map(expenseCategory => {
          return (
              <ExpenseCategory key={expenseCategory.expenseCategoryID} expenseCategory={expenseCategory} />
          )
        });

        return (
            <div className="ManageExpenses">
                <Header/>
                <Col sm="12" md={{ size: 5, offset: 3 }}>

                    <Form className={"manageE"}>
                        <Row className="DateRow">
                            <Col sm="12" md={{ size: 8, offset: 2 }}>
                                <FormGroup>
                                    <Label for="manageExpenses"><h5>Unesite podatke o trošku</h5></Label>
                                </FormGroup>

                                <FormGroup>
                                      <Label  for="date">Datum:</Label>
                                      <Input type="date"
                                             name="date"
                                             id="date"
                                             placeholder="date placeholder"
                                             onChange={this.handleChange}
                                             value={this.state.date}
                                      />
                                 </FormGroup>
                            </Col>
                        </Row>

                        <Row className="Description">
                            <Col sm="12" md={{ size: 8, offset: 2 }}>
                                <FormGroup>
                                     <Label for="description">Opis</Label>
                                     <Input
                                     type="text"
                                     name="description"
                                     id="description"
                                     onChange={this.handleChange}
                                     value={this.state.description}
                                     />
                                </FormGroup>
                            </Col>
                        </Row>

                        <Row className="ExpenseCategory">
                            <Col sm="12" md={{ size: 8, offset: 2 }}>
                                <FormGroup>
                                    <Label for="currentCategory">Kategorija troška</Label>
                                    <Input  type="select"
                                            name="expenseCategoryName"
                                            onChange={this.handleChangeExpenseCategory}
                                            value={this.state.expenseCategoryName}
                                    >
                                        <option>Odaberite kategoriju troška</option>
                                        {usersExpenseCategories}
                                    </Input>
                                </FormGroup>
                            </Col>
                        </Row>


                        <Row className="Amount">
                             <Col sm="12" md={{ size: 8, offset: 2 }}>
                                  <FormGroup>
                                       <Label for="amount">Iznos:</Label>
                                       <Input
                                            type="text"
                                            name="amount"
                                            id="amount"
                                            placeholder="0.00"
                                            onChange={this.handleChange}
                                            value={this.state.amount}
                                       />
                                  </FormGroup>
                             </Col>
                        </Row>

                        <Row>
                            <Col sm="12" md={{ size: 8, offset: 5 }}>
                                <Button onClick={() => this.sendData()}
                                        >POTVRDI</Button>
                            </Col>
                        </Row>

                        <Row>
                            <Col sm="12" md={{ size: 8, offset: 3 }}>
                                <Label>{this.state.info}</Label>
                            </Col>
                        </Row>
                    </Form>
                </Col>

            </div>
        );
    }
}

export default ManageExpenses;