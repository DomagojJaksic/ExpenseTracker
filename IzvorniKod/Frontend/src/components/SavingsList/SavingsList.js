import React, { Component } from 'react';
import {Button, Col, Container, Row, Table} from "reactstrap";
import './SavingsList.css';
import SavingsListRow from "../SavingsListRow/SavingsListRow";



class SavingsList extends Component {

    state= {

        savings: []
    };
    render() {

        let savings = this.props.savings.map(saving => {
            return (
                <SavingsListRow key={saving.savingID}
                                saving={saving}
                                usernameCurrent={this.props.user.username}
                                userID={this.props.user.userID}
                                fetchData={ this.props.fetchData}
                                redirectToSavings={this.props.redirectToSavings}
                                getSavings={this.props.getSavings}

                />
            )
        });

        return (
            <div className="SavingsList">
                <Container className={'cont1'}>
                    <Row>
                        <Col xl="auto">
                            <h2>Popis štednji</h2>
                        </Col>
                        <Col>
                            <Button
                                onClick={() => this.props.addSaving(this.props.user.userID)}
                            >Dodaj</Button>
                        </Col>
                    </Row>
                    <Row>
                        <Table className={"tab2"}
                               bordered hover
                        >
                            <thead>
                                <tr>
                                    <th className={'text-center overflow-auto'} width='17.5%'>Datum početka</th>
                                    <th className={'text-center overflow-auto'} width='17.5%'>Ciljani datum</th>
                                    <th className={'text-center overflow-auto'} width='17.5%'>Trenutni iznos</th>
                                    <th className={'text-center overflow-auto'} width='17.5%'>Ciljani iznos</th>
                                    <th className={'text-center overflow-auto'} width='30%'>Ime</th>
                                </tr>
                            </thead>
                            <tbody>
                            {savings}
                            </tbody>
                        </Table>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default SavingsList;