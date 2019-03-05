import React from "react";
import { Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input, Row, Col } from 'reactstrap';


class Invitation extends React.Component {


    constructor(props) {
        super(props);
    }


    render() {
        let name = this.props.name;
        return (

            <tr className={"red"}>
                <td className={'overflow-auto'}
                    align={"center"}>{name}</td>
                <td className={'overflow-auto'}
                    align={"center"}>
                    <Button color="danger" onClick={() =>
                        this.props.declineInvitation(this.props.id, this.props.user.username)}>
                        ODBIJ
                    </Button>
                </td >
                <td className={"overflow-auto"}
                    align={"center"}>
                        <Button color="success" onClick={() =>
                            this.props.acceptInvitation(this.props.id, this.props.user.username)}>
                            PRIHVATI
                        </Button>
                </td>
             </tr>

        );
    }
}

export default Invitation;