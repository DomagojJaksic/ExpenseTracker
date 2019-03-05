import React from "react";
import {Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input, Row, Col, InputGroup} from 'reactstrap';


class InvitedUser extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { username } = this.props.user;
        return (

            <tr className={"red"}>
                <td className={'overflow-auto'}
                    align={"center"}>{username}</td>
                <td className={'overflow-auto'}
                    align={"center"}>

                    <Button color={'danger'} onClick={() => this.props.cancelInvitation(username)}>
                        Poništi
                    </Button>

                </td>
            </tr>
        );

    }
}

export default InvitedUser;