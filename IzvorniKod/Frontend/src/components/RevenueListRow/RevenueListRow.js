import React, { Component } from 'react';
import {Button} from "reactstrap";
import './RevenueListRow.css';
import dateFormat from "dateformat";

class RevenueListRow extends Component {

    state = {
        disable: true
    }

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

    deleteRevenue = (revenueID) => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        fetch('api/revenues/' + revenueID, options)
            .then(response => {
                if(response.ok) {
                    this.props.fetchData(this.props.userID)
                }
            })
    };



    render() {
        let { date, user,revenueCategory, description,amount } = this.props.revenue;
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
                            onClick={() => this.props.editRevenue(this.props.revenue.revenueID)}
                            disabled={this.state.disable}
                        > uredi</Button>
                    </td >
                    <td className={'overflow-auto'}
                        align={"center"}>
                        <Button color="danger"
                           onClick={() => this.deleteRevenue(this.props.revenue.revenueID)}
                                disabled={this.state.disable}
                        >
                            Obriši
                        </Button>
                    </td>
                </tr>
        );
    }
}

export default RevenueListRow;