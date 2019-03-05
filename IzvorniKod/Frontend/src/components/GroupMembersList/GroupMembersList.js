import React, { Component } from 'react';
import {Button, Col, Container, Row, Table} from "reactstrap";

import './GroupMembersList.css';

import GroupMembersRow from "../GroupMembersRow/GroupMembersRow";




class GroupMembersList extends Component {

    render() {

        let members = this.props.members.map(member => {
            return (
                    <GroupMembersRow key={member.userID}
                                     member={member}
                                     isUserAdmin={this.props.isAdmin}
                                     groupID={this.props.groupID}
                                     savingID={this.props.savingID}
                                     setInfo={this.props.setInfo}
                                     user={this.props.user}
                                     navigateHP={this.props.navigateHP}
                    />
            )
        });

        return (
            <div className="GroupMembersList">
                <Container className={"cont1"}>
                    <Row>
                        <Col xl="auto">
                            <h2>Popis članova</h2>
                        </Col>
                        <Col>
                            <Button onClick={this.props.addNewUser}
                                    disabled={!this.props.isAdmin}>Dodaj</Button>
                        </Col>
                    </Row>
                    <Row>
                        <Table bordered hover>
                            <thead>
                                <tr>
                                    <th className={'text-center overflow-auto'} width="50%">Korisničko ime</th>
                                    <th className={'text-center overflow-auto'} width="25%">Ukloni člana</th>
                                    <th className={'text-center overflow-auto'} width="25%">Postavi za admina</th>
                                </tr>
                            </thead>
                            <tbody>
                                {members}
                            </tbody>
                        </Table>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default GroupMembersList;