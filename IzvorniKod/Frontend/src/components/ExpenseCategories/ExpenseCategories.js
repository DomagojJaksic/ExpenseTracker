import React, { Component } from 'react';
import {Button, Col, Container, Label, Row, Table} from 'reactstrap';
import './ExpenseCategories.css';
import ExpenseCategoriesRow from "../ExpenseCategoriesRow/ExpenseCategoriesRow";
import GroupMembersList from "../GroupMembersList/GroupMembersList";
import RevenueCategoriesRow from "../RevenueCategoriesRow/RevenueCategoriesRow";


class ExpenseCategories extends Component {

    state = {
        expenseCategories: []
    };


    componentDidMount() {
        this.setState({expenseCategories: this.props.expenseCategories})
    }

    render() {
        let expenseCategories = this.props.expenseCategories.map(expenseCategory => {
            if (expenseCategory.timePeriod === "THREE_MONTHS") {
                 expenseCategory.timePeriod = '3 mjeseca';
            }
            if(expenseCategory.timePeriod === "SIX_MONTHS") {
                expenseCategory.timePeriod = '6 mjeseci';
            }
            else if(expenseCategory.timePeriod === "ONE_YEAR") {
                expenseCategory.timePeriod = '1 godina';
            } else if(expenseCategory.timePeriod === "NO_NOTIFICATION") {
                expenseCategory.timePeriod = 'Bez obavijesti';
            }
            return (

                <ExpenseCategoriesRow key={expenseCategory.expenseCategoryID}
                                      expenseCategory={expenseCategory}
                                      setInfo={this.props.setInfo}
                                      groupID={this.props.groupID}
                                      user={this.props.user}
                                      editExpenseCategory={this.props.editExpenseCategory}
                                      isAdmin={this.props.isAdmin}
                                      updateComponent={this.props.updateComponent}


                />

            )
        });
        return (
            <div className="ExpenseCategories">
                <Container className={"cont1"}>
                    <Row>
                        <Col xl="auto">
                            <h2>Kategorija troška</h2>
                        </Col>
                        <Col>
                            <Button onClick={this.props.addNewExpenseCategory}
                                    disabled={!this.props.isAdmin}>
                                Dodaj
                            </Button>
                        </Col>
                    </Row>
                    <Row>
                        <Table  className={'tab2'}
                                bordered hover>
                            <thead>
                                <tr>
                                    <th className={'text-center overflow-auto'}
                                        width='30%'>Ime kategorije</th>
                                    <th className={'text-center overflow-auto'}
                                        width='20%'>Vremenski period</th>
                                    <th className={'text-center overflow-auto'}
                                        width='20%'>Postotak potreban za aktivaciju obavijesti</th>
                                    <th className={'text-center overflow-auto'}
                                        width='15%'>Uredi</th>
                                    <th className={'text-center overflow-auto'}
                                        width='15%'>Obriši</th>
                                </tr>
                            </thead>
                            <tbody>
                                {expenseCategories}
                            </tbody>
                        </Table>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default ExpenseCategories;