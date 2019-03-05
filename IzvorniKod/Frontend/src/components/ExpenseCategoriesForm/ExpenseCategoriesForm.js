import React, { Component } from 'react';
import {Button, Col, Container, Form, FormGroup, Input, Label} from "reactstrap";



class ExpenseCategoriesForm extends Component {

    state = {
        name:'',
        timePeriod:'',
        limitPercentage:''

    };

    isValid = () => {
        const{limitPercentage}=this.state;
        const{}=this.props;
        return limitPercentage<100 && limitPercentage>0 ;

    };


    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };


    render() {
        return (
            <Container className="ExpenseCategoriesForm">
                <h2>Kategorija troška</h2>
                <Form className="form">
                    <Col>
                        <FormGroup>
                            <Label>Naziv:</Label>
                            <Input
                                type="text"
                                name="expenseCategory"
                                id="expenseCategory"
                                placeholder=""
                                onChange={this.handleChange}
                                value={this.state.name}
                            />
                        </FormGroup>
                    </Col>
                    <Col>
                        <FormGroup>
                            <Label for="timePeriod">Period za notifikacije:</Label>
                            <Input type="select" name="timePeriod" id="timePeriod"
                                   value={this.state.timePeriod} onChange={this.handleChange}>
                                <option>Tjedan dana</option>
                                <option>Mjesec dana</option>
                                <option>Tri mjeseca</option>
                                <option>Šest mjeseci</option>
                                <option>Čitavo vrijeme</option>
                            </Input>
                        </FormGroup>
                    </Col>
                    <Col>
                        <FormGroup>
                            <Label for="limitPercentage">Postotak za notifikacije</Label>
                            <Input
                                type="text"
                                name="limitPercentage"
                                id="limitPercentage"
                                placeholder=""
                                onChange={this.handleChange}
                                value={this.state.limitPercentage}
                            />
                        </FormGroup>
                    </Col>
                    <Button type='submit' disabled={!this.isValid()} color="primary">Potvrdi</Button>
                </Form>
            </Container>
        );
    }
}

export default ExpenseCategoriesForm;