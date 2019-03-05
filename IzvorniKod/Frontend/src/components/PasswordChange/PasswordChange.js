import React, { Component } from 'react';
import './PasswordChange.css';
import {Button, Col, Container, Form, FormGroup, Input, Label, Row} from "reactstrap";



class PasswordChange extends Component {

        state = {
            newPassword: '',
            reenteredNewPassword:'',
            info: '',
            isValid: true
        };


        saveNewPassword = () => {
            this.isPasswordValid();
            if(!this.state.isValid) {
                return;
            }
            if(this.state.newPassword === this.state.reenteredNewPassword) {
                const body = {
                    userID: this.props.user.userID,
                    username: this.props.user.username,
                    password: this.state.newPassword,
                    email: this.props.user.email,
                    emailVerified: this.props.user.emailVerified,
                    verificationCode: this.props.user.verificationCode,
                    firstName: this.props.user.firstName,
                    lastName: this.props.user.lastName,
                    currentBalance: this.props.user.currentBalance,
                    lastHomeGroupMembershipChangeTime: this.props.user.lastHomeGroupMembershipChangeTime
                };
                const options = {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(body)
                };
                fetch('api/users/' + this.props.user.userID + '/password', options)
                    .then(response => {
                        if(response.ok) {
                            this.props.navigateHP();
                            this.setState({info: 'Lozinka uspješno promijenjena.'})
                        } else {
                            this.setState({info: 'Promjena lozinke neuspješna'})
                        }
                    })
            } else {
                this.setState({info: 'Lozinke se ne podudaraju.'})
            }
        };


        handleChange = (event) => {
            this.setState({
                [event.target.name]: event.target.value
            });
        };

        isPasswordValid = () => {
            if (this.state.newPassword.length < 5) {
                this.setState({info : "Lozinka mora biti duga barem 5 znakova!"});
                this.setState({isValid: false})
            } else {
                this.setState({isValid: true})
            }
        };

        render() {
                return (
                    <Container className="PasswordChange">
                        <h2>Promijeni lozinku</h2>
                        <Form className="form">
                            <Col>
                                <FormGroup>
                                    <Label for="examplePassword">Nova lozinka:</Label>
                                    <Input
                                        type="password"
                                        name="newPassword"
                                        id="newPassword"
                                        placeholder="Nova lozinka"
                                        onChange={this.handleChange}
                                        value={this.state.newPassword}
                                        invalid={!this.state.isValid}
                                    />
                                </FormGroup>
                            </Col>
                            <Col>
                                <FormGroup>
                                    <Label for="examplePassword">Ponovi novu lozinku</Label>
                                    <Input
                                        type="password"
                                        name="reenteredNewPassword"
                                        id="reenteredNewPassword"
                                        placeholder="Nova lozinka"
                                        onChange={this.handleChange}
                                        value={this.state.reenteredNewPassword}
                                    />
                                </FormGroup>
                            </Col>
                            <Row>
                                <Label>{this.state.info}</Label>
                            </Row>
                            <Button
                                onClick={this.saveNewPassword}>
                                Potvrdi
                            </Button>
                        </Form>
                    </Container>
                );
        }
}

export default PasswordChange;