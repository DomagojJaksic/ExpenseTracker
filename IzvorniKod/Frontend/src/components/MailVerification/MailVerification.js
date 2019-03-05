import React from "react";
import { Button, Form, FormGroup, Label, Input, Row, Col } from 'reactstrap';
import HeaderWelcomePage from "../HeaderWelcomePage/HeaderWelcomePage";
import "./MailVerification.css"
import {Link} from "react-router-dom";
class MailVerification extends React.Component {
    state = {
      username: '',
      code: '',
      message: '',
      user: null
    };

    constructor() {
        super();
    }

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    verify = () => {
        const body = {
            username: this.state.username,
            code: this.state.code
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/users/registration/verify', options)
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({message: 'Neuspješna verifikacija.' });
                } else {
                    this.setState({message: 'Uspješna verifikacija'});
                    this.props.history.push('/');
                }
            })
            // .then(data => data.json())
            // .then(user => this.setState({user: user}));
    };

    render() {
        return (
            <div>
                <HeaderWelcomePage/>
                <Row >
                    <Col sm="12" md={{ size: 5, offset: 3 }}>

                    <Form className={"mailV"}>

                        <Row>
                            <Col sm="12" md={{ size: 8, offset: 0 }}>
                                <Label>
                                    <h4>
                                        <b>Verifikacija e-maila</b>
                                    </h4>
                                </Label>
                            </Col>
                        </Row>
                        <FormGroup>
                            <Col>
                                <Label for="member">Unesite korisničko ime: </Label>
                            </Col>
                            <Col>
                                <Input name="username"
                                       placeholder="korisničko ime"
                                       onChange={this.handleChange}
                                       value={this.state.username}
                                />
                            </Col>
                        </FormGroup>
                        <FormGroup>
                            <Col>
                                <Label for="memberCode">Unesite verifikacijski kod: </Label>
                            </Col>
                            <Col>
                            <Input name="code"
                                       placeholder="verifikacijski kod"
                                       onChange={this.handleChange}
                                       value={this.state.code}
                                />
                            </Col>
                        </FormGroup>

                        <FormGroup>
                            <Label for="message">{this.state.message}</Label>
                        </FormGroup>

                        <Col>
                            <Row>
                                <Col>
                                    <Button onClick={() => this.verify()} className="text-center">
                                        POTVRDI
                                    </Button>
                                </Col>
                                <Col>
                                    <Button color = "link">
                                        <Link to = "/">
                                        Već ste verificirali Vaš korisnički račun?
                                        </Link>
                                    </Button>
                                </Col>
                            </Row>
                        </Col>
                    </Form>
                 </Col>
            </Row>
            </div>


        );
    }

}

export default MailVerification;