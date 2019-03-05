import React, { Component } from 'react';
import {Button, Form, FormGroup, Label, Input, Col, Row, ListGroupItem, Container} from 'reactstrap';
import LoginFailWarning from "../LoginFailWarning/LoginFailWarning";
import HeaderWelcomePage from "../HeaderWelcomePage/HeaderWelcomePage";
import "./LoginForm.css"

class LoginForm extends Component {

    state = {
        username: '',
        password: '',
        loginFail: false

    };

    constructor(props) {
        super(props);
    }

    onSubmit = (e) => {
        e.preventDefault();
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/logout', options)
            .then(response => {
                if (!response.ok) {
                    this.setState({ loginFail: true})
                } else {
                    this.handleLogin(e);
                }
            })
    };

    navMailVerification = () => {
        this.props.history.push("/mailVerification")
    };

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    handleLogin = (e) => {
        e.preventDefault();
        const body = `username=${this.state.username}&password=${this.state.password}`;
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: body
        };
        fetch('login', options)
            .then(response => {
                if (response.status === 401) {
                    this.setState({ loginFail: true})
                } else {
                    // this.handleLogin();
                    this.props.history.push("/homepage");
                }
            })
    };

    navReg = () => {
      this.props.history.push("/register")
    };

    render() {

        return (
            <div>
            <HeaderWelcomePage/>
                <Container >
                    <Row >
                        <Col sm="12" md={{ size: 6, offset: 3 }}>
                            <Form onSubmit={this.onSubmit} className={"loginForm"}>
                                <FormGroup>
                                    <Label>
                                        <h4>
                                            <b>Prijava</b>
                                        </h4>
                                    </Label>
                                </FormGroup>
                                    <FormGroup>
                                        <Label for="username">Korisničko ime:</Label>
                                        <Input
                                            type="text"
                                            name="username"
                                            placeholder="Unesite korisničko ime"
                                            onChange={this.handleChange}
                                            value={this.state.username}
                                            autofocus
                                        />
                                    </FormGroup>
                                    <FormGroup>
                                        <Label for="password">Lozinka: </Label>
                                        <Input
                                            type="password"
                                            name="password"
                                            id="password"
                                            placeholder="Unesite lozinku"
                                            value={this.state.password}
                                            onChange={this.handleChange}
                                        />
                                    </FormGroup>
                                <FormGroup>
                                    <LoginFailWarning   navMailVerification = {this.navMailVerification}
                                                        loginFail = {this.state.loginFail} />
                                </FormGroup>
                                <Row>
                                    <Col>
                                        <Button type="submit" color="secondary" block>POTVRDI</Button>
                                    </Col>
                                    <Col>
                                        <Button onClick={this.navReg}
                                                color="link">Registrirajte se</Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Col>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default LoginForm;