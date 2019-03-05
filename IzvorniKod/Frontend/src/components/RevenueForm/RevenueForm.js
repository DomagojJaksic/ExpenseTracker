import React, { Component } from 'react';
import {Button, Col, Container, Form, FormGroup, Input, Label, Row} from "reactstrap";
import RevenueCategory from "../RevenueCategory/RevenueCategory";





class RevenueForm extends Component {

    state={
        revenueID:'',
        date:'',
        user:'',
        revenueCategory:'',
        revenueCategories: [],
        description:'',
        amount:''
    };

    constructor(props) {
        super(props);
    }


    isValid = () => {

        const{amount}=this.state;
        return amount<0;

    };

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };
    id = 1;

    getRevenueCategories() {
        fetch('api/users/' + this.id + '/revenuecategories')
            .then(response => response.json())
            .then(revenueCategories => this.setState({ revenueCategories }));
    }

    componentDidMount() {
        this.getRevenueCategories()
    }

    render() {

        let usersRevenueCategories = this.state.revenueCategories.map(revenueCategory => {
            return (

                <RevenueCategory key={revenueCategory.revenueCategoryID} revenueCategory={revenueCategory} />

            )
        });


        return (
            <Container className="RevenueForm">
                <h2>Unesite parametre prihoda:</h2>
                <Row >
                    <Form >
                        <Row form>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="date">Datum prihoda: </Label>
                                    <Input type="date" name="date"
                                           id="date" onChange={this.handleChange}
                                           value={this.state.date}/>
                                </FormGroup>
                            </Col>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="amount">Iznos:</Label>
                                    <Input type="text" name="amount"
                                           id="amount" onChange={this.handleChange}
                                           value={this.state.amount}/>
                                </FormGroup>
                            </Col>
                            <Col>
                                <FormGroup>
                                    <Label for="revenueCategory">Odaberite kategoriju prihoda:</Label>
                                    <Input  type="select"
                                            name="currentCategory"
                                            onChange={this.handleChange}
                                            value={this.state.revenueCategory}
                                    >
                                        {usersRevenueCategories}
                                    </Input>
                                </FormGroup>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Button type='submit' disabled={!this.isValid()}>Potvrdi</Button>
                            </Col>
                        </Row>
                    </Form>
                </Row>
            </Container>
        );
    }
}

export default RevenueForm;