import React, { Component } from 'react';
import {Button, Col, Container, Row, Table} from "reactstrap";
import './RevenueList.css';
import RevenueListRow from "../RevenueListRow/RevenueListRow";

class RevenueList extends Component {

    state= {

        revenues: []
    };

    render() {

        let revenues = this.props.revenues.map(revenue => {
            let name;
            try {
                name = revenue.revenueCategory.name;
            } catch {
                name = 'nepoznato';
            }
            return (
                    <RevenueListRow key={revenue.revenueID}
                                    revenue={revenue}
                                    username={revenue.user.username}
                                    userID={this.props.user.userID}
                                    name={name}
                                    usernameCurrent={this.props.usernameCurrent}
                                    fetchData={this.props.fetchData}
                                    editRevenue={this.props.editRevenue}
                    />
            )
        });

        return (
            <div className="RevenueList">
                <Container className={'cont1'}>
                    <Row>
                        <Col xl="auto">
                            <h2>Popis prihoda</h2>
                        </Col>
                        <Col>
                            <Button
                                onClick={() => this.props.addRevenue(this.props.user.userID)}
                            >Dodaj</Button>
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
                                {revenues}
                            </tbody>
                        </Table>
                    </Row>
                </Container>

            </div>
        );
    }
}

export default RevenueList;