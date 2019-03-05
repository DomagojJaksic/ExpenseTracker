import React, { Component } from 'react';
import {Button} from "reactstrap";
import './ExpenseListRow.css';
import dateFormat from "dateformat";

class ExpenseListRow extends Component {

    state = {
        disable: true
    };

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        if(this.props.usernameCurrent !== this.props.username) {
            this.setState({disable: true})
        } else {
            this.setState({disable: false})
        }
    }

    deleteExpense = (expenseID) => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch('api/expenses/' + expenseID, options)
            .then(response => {
                if(response.ok) {
                    this.props.fetchData(this.props.userID)
                    //(this.props.userID);
                }
            })
    };



    render() {
        let { date, user,expenseCategory, description,amount } = this.props.expense;
        return (
                <tr className={"red"}>
                    <td className={'overflow-auto'}
                        align={"center"}>{dateFormat(date,'dd.mm.yyyy.')}</td>
                    <td className={'overflow-auto'}
                        align={"center"}>{user.username}</td>
                    <td className={'overflow-auto'}
                        align={"center"}>{this.props.name}</td>
                    <td className={'overflow-auto'}
                        align={"center"}>{description}</td>
                    <td className={'overflow-auto'}
                        align={"center"}>{amount}</td>
                    <td className={'overflow-auto'}
                        align={"center"}>
                        <Button
                            color="link"
                            onClick={() => this.props.editExpense(this.props.expense.expenseID)}
                            disabled={this.state.disable}
                        > uredi</Button>
                    </td >
                    <td headers={'th7'}
                        className={"delete"}
                        align={"center"}>
                        <Button color="danger"
                                onClick={() => this.deleteExpense(this.props.expense.expenseID)}
                                disabled={this.state.disable}
                        >Obriši</Button>
                    </td>
                </tr>
        );
    }
}

export default ExpenseListRow;