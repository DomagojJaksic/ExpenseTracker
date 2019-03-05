import React, { Component } from 'react';
import {Button, Col, Container, Row, Table} from "reactstrap";
import './ExpenseList.css';
import ExpenseListRow from "../ExpenseListRow/ExpenseListRow";

class ExpenseList extends Component {

    state= {
        expenses: []
    };


    render() {

        let expenses = this.props.expenses.map(expense => {
            let name;
            try {
                name = expense.expenseCategory.name;
            } catch {
                name = 'nepoznato';
            }
            return (
                    <ExpenseListRow key={expense.expenseID}
                                    expense={expense}
                                    username={expense.user.username}
                                    userID={this.props.user.userID}
                                    name={name}
                                    usernameCurrent={this.props.usernameCurrent}
                                    fetchData={this.props.fetchData}
                                    editExpense={this.props.editExpense}
                    />
            )
        });

        return (
            <div className="ExpenseList">
                <Container className={'cont1'}>
                    <Row>
                        <Col xl="auto">
                            <h2>Popis troškova</h2>
                        </Col>
                        <Col>
                            <Button
                                onClick={() => this.props.addExpense(this.props.user.userID)}
                            >Dodaj
                            </Button>
                        </Col>
                    </Row>
                    <Row>
                        <Table className={"tab2"}
                            bordered hover
                        >
                            <thead>
                                <tr>
                                    <th className={'text-center overflow-auto'} width='12%'>Datum</th>
                                    <th className={'text-center overflow-auto'} width='15%'>Korisnik</th>
                                    <th className={'text-center overflow-auto'} width='15%'>Kategorija</th>
                                    <th className={'text-center overflow-auto'} width='25%'>Opis</th>
                                    <th className={'text-center overflow-auto'} width='13%'>Iznos(HRK)</th>
                                    <th className={'text-center overflow-auto'} width='10%'>Uredi</th>
                                    <th className={'text-center overflow-auto'} width='10%'>Obriši</th>
                                </tr>
                            </thead>
                            <tbody>
                                {expenses}
                            </tbody>
                        </Table>
                    </Row>
                </Container>

            </div>
        );
    }
}

export default ExpenseList;