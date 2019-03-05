import React, { Component } from 'react';
import {Button, Col, Container, Row, Table} from "reactstrap";
import './NotificationList.css';
import NotificationListRow from "../NotificationListRow/NotificationListRow";
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';

class NotificationList extends Component {

    state= {

        notifications:[]
    };

    render() {

        let notifications = this.props.notifications.map(notification => {
            return (
                    <NotificationListRow key={notification.notificationID}
                                         notification={notification}
                                         fetchData={this.props.fetchData}
                    />
            )
        });

        return (
            <div className="NotificationList">
                <Container className={'cont1'}>
                    <Row>
                        <Col xl="auto">
                            <h2>Obavijesti</h2>
                        </Col>
                    </Row>
                    <Row>
                        <Table bordered hover
                                className={"tab2"}>
                            <thead>
                                <tr>
                                    <th className={'text-center overflow-auto'} width='20%'>Datum</th>
                                    <th className={'text-center overflow-auto'} width='80%' >Poruka</th>
                                </tr>
                            </thead>
                            <tbody>
                                {notifications}
                            </tbody>
                        </Table>
                    </Row>
                </Container>


            </div>
        );
    }
}

export default NotificationList;