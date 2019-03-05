import React from "react";
import { Button, Form, FormGroup, ListGroup, ListGroupItem, Label, Input, Row, Col } from 'reactstrap';

class ExpenseCategory extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {

        let { name } = this.props.expenseCategory;

        return (
            <option value={name}>
                {name}
            </option>
        );

    }

}

export default ExpenseCategory;