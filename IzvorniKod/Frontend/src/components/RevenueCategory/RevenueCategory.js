import React from "react";
import { Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input, Row, Col } from 'reactstrap';


class RevenueCategory extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {

        let { name } = this.props.revenueCategory;

        return (
            <option value={name}>
                {name}
            </option>
        );

    }

}

export default RevenueCategory;