import React, { Component } from 'react';
import {Button, Col, Container, Form, FormGroup, Input, Label, Row} from "reactstrap";
import './SavingsForm.css';



class SavingsForm extends Component {

    state={
        name:'',
        currentBalance:'',
        targetedAmount:'',
        startDate:'',
        endDate:''
    };


    isValid = () => {
        const{currentBalance, targetedAmount, startDate,endDate}=this.state;
        return currentBalance>=0 && currentBalance<targetedAmount && startDate<endDate  ;
    };


    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    render() {
        return (
            <Container className="SavingsForm">
                <h2>Unesite parametre štednje</h2>
                <Row >
                    <Form >
                        <Row form>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="savingName">Ime štednje: </Label>
                                    <Input type="text" name="savingName" placeholder=""
                                           id="savingName" onChange={this.handleChange}
                                           value={this.state.name}/>
                                </FormGroup>
                            </Col>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="currentBalance">Trenutni iznos: </Label>
                                    <Input type="text" name="currentBalance" placeholder="0.00"
                                           id="currentBalance" onChange={this.handleChange}
                                           value={this.state.currentBalance}/>
                                </FormGroup>
                            </Col>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="targetedAmount">Ciljani iznos:</Label>
                                    <Input type="text" name="targetedAmount"
                                           placeholder="0.00"
                                           id="targetedAmount" onChange={this.handleChange}
                                           value={this.state.targetedAmount}/>
                                </FormGroup>
                            </Col>
                            <Col>
                                <FormGroup>
                                    <Label for="startDate">Datum početka</Label>
                                    <Input type="date" name="startDate"
                                           id="startDate" onChange={this.handleChange}
                                           value={this.state.startDate}/>
                                </FormGroup>
                            </Col>
                            <Col >
                                <FormGroup>
                                    <Label for="endDate">Ciljani datum</Label>
                                    <Input type="date" name="endDate"
                                           id="endDate" onChange={this.handleChange}
                                           value={this.state.endDate}/>
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

export default SavingsForm;