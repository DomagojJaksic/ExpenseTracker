import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input } from 'reactstrap';
import './ManageRevenues.css';
import RevenueCategory from '../RevenueCategory/RevenueCategory';
import Header from "../Header/Header";


class ManageRevenues extends Component {

	state = {
	    revenueCategories: [],
	    revenueID: -1,
	    user: null,
		date: '',
		description: '',
		amount: '',
        entryTime: '',
        revenueCategoryName: '',
		revenueCategory: null,
        info: '',
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

    handleChangeRevenueCategory = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });

        for (let i = 0; i < this.state.revenueCategories.length; i++) {
            if(this.state.revenueCategories[i].name === event.target.value) {
                this.setState({revenueCategory: this.state.revenueCategories[i]});
            }
        }
    };

    sendData = () => {
        if(this.state.isEdit) {
            this.editRevenue();
        } else {
            this.addRevenue();
        }
    };

    addRevenue = () => {
        const body = {
            user: this.state.user,
            description: this.state.description,
            date: this.state.date,
            amount: this.state.amount,
            revenueCategory: this.state.revenueCategory
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/revenues/', options)
            .then(response => {
                if(response.ok) {
                    this.props.history.push('/homepage');
                    this.setState({info: 'Prihod uspješno dodan.'})
                } else {
                    this.setState({info: 'Neuspješno dodavanje prihoda.'})
                }
            })
    };


    editRevenue = () => {
        const body = {
            revenueID: this.state.revenueID,
            entryTime: this.state.entryTime,
            user: this.state.user,
            description: this.state.description,
            date: this.state.date,
            amount: this.state.amount,
            revenueCategory: this.state.revenueCategory
        };
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/revenues/' + this.state.revenueID, options)
            .then(response => {
                if(response.ok) {
                    this.props.history.push('/homepage');
                    this.setState({info: 'Uređivanje prihoda uspješno.'})
                } else {
                    this.setState({info: 'Neuspješno uređivanje prihoda.'})
                }
            })
    };

    getRevenueCategories = (id) => {
        fetch('api/users/' + id + '/revenuecategories')
            .then(response => response.json())
            .then(revenueCategories => this.setState({ revenueCategories }));
    };

    fetchRevenue = (id) => {
        fetch('api/revenues/' + id)
            .then(response => response.json())
            .then(revenue => {
                this.setState({revenueID: revenue.revenueID});
                this.setState({amount: revenue.amount});
                this.setState({description: revenue.description});
                this.setState({entryTime: revenue.entryTime});
                this.setState({date: revenue.date});
                this.setState({user: revenue.user});
                this.setState({revenueCategory: revenue.revenueCategory});
                this.setState({revenueCategoryName: revenue.revenueCategory.name});
            });
    };

    componentDidMount() {
            if (this.props.location.state.isEdit) {
                this.setState({isEdit: true});
                this.fetchRevenue(this.props.location.state.revenueID);
            }

            fetch('api/users/current')
                .then(response => response.json())
                .then(response => {
                    this.setState({user: response});
                    this.getRevenueCategories(response.userID);
                })
        // }
    }


    render() {
      let usersRevenueCategories = this.state.revenueCategories.map(revenueCategory => {
          return (
                <RevenueCategory key={revenueCategory.revenueCategoryID} revenueCategory={revenueCategory} />
          )
      });

      return (
            <div className="ManageRevenues">
                <Header/>
                <Col sm="12" md={{ size: 5, offset: 3 }}>
                <Form className={"manageR"}>
                    <Row className="DateRow">
                        <Col sm="12" md={{ size: 8, offset: 2 }}>
                            <FormGroup>
                                <Label for="manageRevenues"><h5>Unesite podatke o prihodu</h5></Label>
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
                                 onChange = {this.handleChange}
                                 value={this.state.description}
                                 />
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row className="RevenueCategory">
                        <Col sm="12" md={{ size: 8, offset: 2 }}>
                            <FormGroup>
                                <Label for="currentCategory">Kategorija prihoda</Label>
                                <Input  type="select"
                                        name="revenueCategoryName"
                                        onChange={this.handleChangeRevenueCategory}
                                        value={this.state.revenueCategoryName}
                                >
                                    <option> Izaberite kategoriju prihoda </option>
                                    {usersRevenueCategories}
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

export default ManageRevenues;