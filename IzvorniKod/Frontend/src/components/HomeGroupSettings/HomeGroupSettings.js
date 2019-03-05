import React, { Component } from 'react';
import {Button, ButtonGroup, Col, Container, Label, Row} from "reactstrap";
import "./HomeGroupSettings.css"


class HomeGroupSettings extends Component {


    state = {
        isGroupMember: false,
        buttonText: ''
    };

    componentDidMount() {
        if(this.props.isGroupMember) {
            this.setState({isGroupMember: 'NAPUSTI'})
        } else {
            this.setState({buttonText: 'STVORI'})
        }
    }


    render() {
        return (
            <div className="HomeGroupSettings">
                <Container>
                    <Row>
                        <Col>
                            <label><h3>Grupa: </h3></label>
                        </Col>
                        <Col>
                            <Button onClick={this.props.buttonAction}>
                                {this.props.buttonText}
                            </Button>
                        </Col>
                    </Row>
                </Container>

            </div>
        );
    }
}

export default HomeGroupSettings;