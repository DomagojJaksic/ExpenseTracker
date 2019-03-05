import React, { Component } from 'react';
import {Button, Col, Container, Form, FormGroup, Input, Label} from "reactstrap";



class RevenueCategoriesForm extends Component {

    state = {
        name:''

    };

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    render() {
        return (
            <Container className="ExpenseCategoriesForm">
                <h2>Kategorija prihoda</h2>
                <Form className="form">
                    <Col>
                        <FormGroup>
                            <Label>Naziv</Label>
                            <Input
                                type="text"
                                name="revenueCategory"
                                id="revenueCategory"
                                placeholder=""
                                onChange={this.handleChange}
                                value={this.state.name}
                            />
                        </FormGroup>
                    </Col>
                    <Button type='submit'  color="primary">Potvrdi</Button>
                </Form>
            </Container>
        );
    }
}

export default RevenueCategoriesForm;