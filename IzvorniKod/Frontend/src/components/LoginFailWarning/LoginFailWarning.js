import {Button, Col, Label, Row} from "reactstrap";
import React, {Component} from "react";
import {Link} from "react-router-dom";

class LoginFailWarning extends Component {

    render() {
        const {loginFail} = this.props;
        if(loginFail) {
            return (
                <Row>
                    <Col>
                        <Label for="password">Prijava je neuspješna. Ukoliko niste verificirali Vaš korisnički račun
                            možete to učiniti odabirom poveznice.</Label>

                        <Button color="link">
                            <Link to={"/mailVerification"}>
                                e-mail verifikacija
                            </Link>
                        </Button>
                    </Col>
                </Row>
            )
        } else {
            return null;
        }
    }
}

export default LoginFailWarning;