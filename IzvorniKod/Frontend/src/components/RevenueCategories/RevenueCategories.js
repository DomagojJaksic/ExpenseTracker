import React, { Component } from 'react';
import {Button, Col, Container, Row, Table} from 'reactstrap';
import './RevenueCategories.css';
import RevenueCategoriesRow from "../RevenueCategoriesRow/RevenueCategoriesRow";



class RevenueCategories extends Component {

    state = {
        revenueCategories: []
    };


    render() {
        let revenueCategories = this.props.revenueCategories.map(revenueCategory => {
            return (
                    <RevenueCategoriesRow key={revenueCategory.revenueCategoryID}
                                          revenueCategory={revenueCategory}
                                          setInfo={this.props.setInfo}
                                          groupID={this.props.groupID}
                                          user={this.props.user}
                                          editRevenueCategory={this.props.editRevenueCategory}
                                          isAdmin={this.props.isAdmin}
                                          updateComponent={this.props.updateComponent}
                    />
            )
        });

        return (
            <div className="RevenueCategories">
                <Container className={"cont1"}>
                    <Row>
                        <Col xl="auto">
                            <h2>Kategorija prihoda</h2>
                        </Col>
                        <Col>
                            <Button onClick={this.props.addNewRevenueCategory}
                                    disabled={!this.props.isAdmin}
                            >Dodaj</Button>
                        </Col>
                    </Row>
                    <Row>
                        <Table  className={'tab2'}
                                bordered hover>
                            <thead>
                                <tr>
                                    <th className={'text-center overflow-auto'} width='50%'>Ime kategorije</th>
                                    <th className={'text-center overflow-auto'} width='25%'>Uredi</th>
                                    <th className={'text-center overflow-auto'} width='25%'>Obriši</th>
                                </tr>
                            </thead>
                            <tbody>
                                {revenueCategories}
                            </tbody>
                        </Table>
                    </Row>
                </Container>


            </div>
        );
    }
}

export default RevenueCategories;