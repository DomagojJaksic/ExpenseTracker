import React, { Component } from 'react';
import { Row, Col, Button, Form, FormGroup, FormFeedback, FormText, Label, Input } from 'reactstrap';
import HeaderWelcomePage from "../HeaderWelcomePage/HeaderWelcomePage";
import "./RegisterForm.css"
import {Link} from "react-router-dom";
class RegisterForm extends Component {
    state = {
        firstName: "",
        lastName: "",
        email: "",
        username: "",
        password: "",
        passwordRepeat: "",
        errorMessage: "",
        formErrors: {
            firstName: [],
            lastName: [],
            email: [],
            username: [],
            password: [],
            passwordRepeat: []
        },

        validity: {
            firstName: true,
            lastName: true,
            email: true,
            username: true,
            password: true,
            passwordRepeat: true
        },
        formIsValid: false,
        usernameExists: false,
        emailExists: false

    };

    validateForm = () => {

        let fieldValidationErrors = this.state.formErrors;
        let fieldValidity = this.state.validity;
        let newErrors = [];

        fieldValidity.firstName = true;
        if (!/^[áàíóúšđžčćéëöüñÄĞİŞȘØøğışÐÝÞðýþA-Za-z-']+$/i.test(this.state.firstName)) {
            newErrors.push("Ime mora sadržavati samo slova abecede!");
            fieldValidity.firstName = false;
        }
        if (this.state.firstName.length < 2) {
            newErrors.push("Ime mora biti dugo barem dva znaka!");
            fieldValidity.firstName = false;
        }

        fieldValidationErrors.firstName = newErrors;
        newErrors = [];

        fieldValidity.lastName = true;
        if (!/^[áàíóúšđžčćéëöüñÄĞİŞȘØøğışÐÝÞðýþA-Za-z-']+$/i.test(this.state.lastName)) {
            newErrors.push("Prezime mora sadržavati samo slova abecede!");
            fieldValidity.lastName = false;
        }
        if (this.state.lastName.length < 2) {
            newErrors.push("Prezime mora biti dugo barem dva znaka!");
            fieldValidity.lastName = false;
        }
        fieldValidationErrors.lastName = newErrors;
        newErrors = [];

        fieldValidity.email = true;
        if (!/^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i.test(this.state.email)) {
            newErrors.push("Neispravna e-mail adresa!");
            fieldValidity.email = false;
        }
        if(this.state.emailExists) {
            newErrors.push("Korisnik s ovom e-mail adresom već postoji!");
            fieldValidity.email = false;
        }
        fieldValidationErrors.email = newErrors;
        newErrors = [];

        fieldValidity.username = true;
        if (this.state.username.length < 3) {
            newErrors.push("Korisničko ime mora sadržavati barem 3 znaka!");
            fieldValidity.username = false;
        }
        if(this.state.usernameExists) {
            newErrors.push("Korisnik s ovim korisničkim imenom već postoji!");
            fieldValidity.username = false;
        }
        fieldValidationErrors.username = newErrors;
        newErrors = [];

        fieldValidity.password = true;
        if (this.state.password.length < 5) {
            newErrors.push("Lozinka mora biti duga barem 5 znakova!");
            fieldValidity.password = false;
        }
        fieldValidationErrors.password = newErrors;
        newErrors = [];

        fieldValidity.passwordRepeat = true;
        if (this.state.password !== this.state.passwordRepeat) {
            newErrors.push("Lozinke se ne podudaraju!");
            fieldValidity.passwordRepeat = false;
        }
        fieldValidationErrors.passwordRepeat = newErrors;


        this.setState({
            validity: fieldValidity,
            formErrors: fieldValidationErrors
        });

        let { firstName, lastName, email, username, password, passwordRepeat } = this.state.validity;
        let formValidity = firstName && lastName && email && username && password && passwordRepeat;
        this.setState({ formIsValid: formValidity });
        if(formValidity) {
            this.setState({errorMessage: "Molimo pričekajte, Vaš zahtjev je trenutačno u obradi."});
            this.handleRegister();
        }
    };


    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    handleChangeEmail = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });

        this.emailExists(event.target.value);

    };

    handleChangeUsername = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });

        this.usernameExists(event.target.value);

    };


    usernameExists = (username) => {
        fetch('api/users/username/' + username)
            .then(response => {
                if(response.status >= 400 && response.status < 600) {
                    this.setState({usernameExists: false});
                } else {
                    this.setState({usernameExists: true});
                }
            })
    };

    emailExists = (email) => {
        fetch('api/users/email/' + email)
            .then(response => {
                if(response.status >= 400 && response.status < 600) {
                    this.setState({emailExists: false});
                } else {
                    this.setState({emailExists: true});
                }
            })
    };


    handleRegister = () => {
        const body = {
            username: this.state.username,
            password: this.state.password,
            email: this.state.email,
            firstName: this.state.firstName,
            lastName: this.state.lastName
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/users/registration', options)
            .then(response => {
                if (!response.ok) {
                    this.setState({errorMessage: "Registracija neuspješna."});
                } else {
                    this.navigateMV();
                }
            })

    };

    navigateMV = () => {
        this.props.history.push('/mailVerification');
    };

    render() {

        return (
            <div>
            <HeaderWelcomePage/>
            <Row>
                <Col sm="12" md={{ size: 6, offset: 3 }}>
                    <Form onSubmit={this.handleRegister} className={"regForm"}>
                        <FormGroup>
                            <Label>
                                <h4>
                                    <b>Registracija</b>
                                </h4>
                            </Label>
                        </FormGroup>
                            <Row form>
                                <Col md={6}>

                                    <FormGroup>
                                        <Label for="name">Ime: </Label>
                                        <Input
                                            type="text"
                                            name="firstName"
                                            placeholder="Ime"
                                            value={this.state.firstName}
                                            onChange={this.handleChange}
                                            autoFocus
                                            invalid={!this.state.validity.firstName}
                                            required
                                        />
                                        {this.state.formErrors.firstName.map(errorMessage =>
                                            <FormFeedback invalid>{errorMessage}</FormFeedback>)}
                                    </FormGroup>
                                </Col>
                                <Col md={6}>
                                    <FormGroup>
                                        <Label for="surname">Prezime: </Label>
                                        <Input
                                            type="text"
                                            name="lastName"
                                            placeholder="Prezime"
                                            value={this.state.lastName}
                                            onChange={this.handleChange}
                                            invalid={!this.state.validity.lastName}
                                            required
                                        />
                                        {this.state.formErrors.lastName.map(errorMessage =>
                                            <FormFeedback invalid>{errorMessage}</FormFeedback>)}
                                    </FormGroup>
                                </Col>
                            </Row>
                        <FormGroup>
                            <Label for="email">E-mail adresa: </Label>
                            <Input
                                type="email"
                                name="email"
                                placeholder="Email"
                                value={this.state.email}
                                onChange={this.handleChangeEmail}
                                invalid={!this.state.validity.email}// TODO check email
                                required
                            />
                            {this.state.formErrors.email.map(errorMessage =>
                                <FormFeedback invalid>{errorMessage}</FormFeedback>)}
                        </FormGroup>
                        <FormGroup>
                            <Label for="username">Korisničko ime: </Label>
                            <Input
                                type="text"
                                name="username"
                                placeholder="Korisničko ime"
                                value={this.state.username}
                                onChange={this.handleChangeUsername}
                                invalid={!this.state.validity.username}
                                required
                            />
                            {this.state.formErrors.username.map(errorMessage =>
                                <FormFeedback invalid>{errorMessage}</FormFeedback>)}
                            <FormText>Ovo je jedinstveno ime kojim ćete biti prepoznavani unutar aplikacije.</FormText>
                        </FormGroup>
                        <FormGroup>
                            <Label for="password">Lozinka: </Label>
                            <Input
                                type="password"
                                name="password"
                                placeholder="Lozinka"
                                value={this.state.password}
                                onChange={this.handleChange}
                                invalid={!this.state.validity.password || !this.state.validity.passwordRepeat}
                                required
                            />
                            {this.state.formErrors.password.map(errorMessage =>
                                <FormFeedback invalid>{errorMessage}</FormFeedback>)}
                        </FormGroup>
                        <FormGroup>
                            <Label for="passwordRepeat">Ponovite lozinku: </Label>
                            <Input
                                type="password"
                                name="passwordRepeat"
                                placeholder="Lozinka"
                                value={this.state.passwordRepeat}
                                onChange={this.handleChange}
                                invalid={!this.state.validity.passwordRepeat}
                                required
                            />
                            {this.state.formErrors.passwordRepeat.map(errorMessage =>
                                <FormFeedback invalid>{errorMessage}</FormFeedback>)}
                        </FormGroup>

                        <FormGroup>
                            <Label for="message">{this.state.errorMessage}</Label>
                        </FormGroup>
                        <Row>
                            <Col>
                                <Button onClick={this.validateForm} color="secondary" block>POTVRDI</Button>
                            </Col>
                            <Col>
                                <Button color="link" block><Link to={"/"}>Već imate korisnički račun?</Link></Button>
                            </Col>
                        </Row>
                    </Form>
                </Col>
            </Row>
            </div>
        );
    }
}

export default RegisterForm;