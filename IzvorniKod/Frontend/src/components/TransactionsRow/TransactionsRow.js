import React, { Component } from 'react';
import {Button} from "reactstrap";
import dateFormat from "dateformat";



class TransactionsRow extends Component {

    state = {
      user: {
          userID: -1,
          username: ''
      },
      isEditable: true

    };

    componentDidMount() {
        fetch('api/users/' + this.props.transaction.user.userID)
            .then(response => response.json())
            .then(response => {
                this.setState({user: response})
            });
        fetch('api/transactions/' + this.props.transaction.savingTransactionID + '/editable')
            .then(response => {
                if(!response.ok) {
                    this.setState({isEditable: false})
                }
            });
    }

    constructor(props) {
        super(props);
    }

    deleteTransaction = () => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/transactions/' + this.props.transaction.savingTransactionID, options)
            .then(response => {
                this.props.setInfo(this.props.savingID, this.props.user.userID);
            })
    };

    editST = () => {
        this.props.editTransaction(this.props.transaction.savingTransactionID);
    };

    render() {
        let { time, username, userID ,type,amount } = this.props.transaction;
        return (
            <tr>
                <td className={'overflow-auto'}
                    align={"center"}>{this.props.transaction.user.username}</td>
                <td className={'overflow-auto'}
                    align={"center"}>{dateFormat(time,'dd.mm.yyyy.')}</td>
                <td className={'overflow-auto'}
                    align={"center"}>{type}</td>
                <td className={'overflow-auto'}
                    align={"center"}>{amount}</td>
                <td className={'overflow-auto'}
                    align={"center"}>
                    <Button color="link"
                            onClick={this.editST}
                            disabled={this.props.transaction.user.userID !== this.props.user.userID
                            || !this.state.isEditable}
                    > uredi</Button>
                </td>
                <td className={'overflow-auto'}
                    align={"center"}>
                    <Button color="danger"
                            onClick={this.deleteTransaction}
                            disabled={this.props.transaction.user.userID !== this.props.user.userID}
                    >
                        Obriši
                    </Button>
                </td>
            </tr>
     );
    }
}

export default TransactionsRow;