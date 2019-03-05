import React, { Component } from 'react';
import {Button, Col, Container, Label, Row, Table} from "reactstrap";
import './Transactions.css';
import TransactionsRow from "../TransactionsRow/TransactionsRow";


class Transactions extends Component {


    render() {

            let transactions = this.props.transactions.map(transaction => {
                if(transaction.type === 'DEPOSIT') {
                    transaction.type = 'Uplata';
                } else if(transaction.type === 'WITHDRAW') {
                    transaction.type = 'Isplata';
                }
            return (
                    <TransactionsRow key={transaction.savingTransactionID}
                                     transaction={transaction}
                                     member={transaction.user}
                                     user={this.props.user}
                                     savingID={this.props.savingID}
                                     setInfo={this.props.setInfo}
                                     addNewTransaction={this.props.addNewTransaction}
                                     editTransaction={this.props.editTransaction}
                    />
            )
        });
        return (
            <div className="Transactions">
                <Container className={"cont1"}>
                    <Row>
                        <Col xl="auto">
                            <h2>Transakcije</h2>
                        </Col>
                        <Col>
                            <Button onClick={this.props.addNewTransaction}>Dodaj</Button>
                        </Col>
                        <Col>
                            <Label><h5>{this.props.info}</h5></Label>
                        </Col>
                    </Row>
                    <Row>
                        <Table bordered hover
                                className={"tab2"}>
                            <thead>
                                <tr>
                                    <th className={'text-center overflow-auto'} width="20%">Korisnik</th>
                                    <th className={'text-center overflow-auto'} width="20%">Datum</th>
                                    <th className={'text-center overflow-auto'} width="20%">Vrsta</th>
                                    <th className={'text-center overflow-auto'} width="16%">Iznos</th>
                                    <th className={'text-center overflow-auto'} width="12%">Uredi</th>
                                    <th className={'text-center overflow-auto'} width="12%">Obriši</th>

                                </tr>
                            </thead>
                            <tbody>
                                {transactions}
                            </tbody>
                        </Table>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default Transactions;