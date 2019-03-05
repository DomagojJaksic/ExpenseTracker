import React, { Component } from 'react';
import {Button} from "reactstrap";
import '../ExpenseList/ExpenseList.css'
import './ExpenseCategoriesRow.css'
import 'bootstrap/dist/css/bootstrap.css';



class ExpenseCategoriesRow extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        if (this.props.expenseCategory.timePeriod === "THREE_MONTHS") {
            this.props.expenseCategory.timePeriod = '3 mjeseca';
        }
         if(this.props.expenseCategory.timePeriod === "SIX_MONTHS") {
            this.props.expenseCategory.timePeriod = '6 mjeseci';
        }
        else if(this.props.expenseCategory.timePeriod === "ONE_YEAR") {
            this.props.expenseCategory.timePeriod = '1 godina';
        } else if(this.props.expenseCategory.timePeriod === "NO_NOTIFICATIONS") {
            this.props.expenseCategory.timePeriod = 'Bez obavijesti';
        }
    }

    deleteExpenseCategory = () => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/expensecategories/' + this.props.expenseCategory.expenseCategoryID, options)
            .then(response => {
                if(this.props.groupID > 0) {
                    this.props.setInfo(this.props.groupID, this.props.user.userID);
                } else {
                    this.props.updateComponent(this.props.user.userID)
                }
            })
    };

    editEC = () => {
        this.props.editExpenseCategory(this.props.expenseCategory.expenseCategoryID);
    };

    render() {
        let { name, timePeriod,limitPercentage } = this.props.expenseCategory;
        return (
            <tr>
                <td align={"center"} className={'overflow-auto'}>{name}</td>
                <td align={"center"} className={'overflow-auto'}>{timePeriod}</td>
                <td align={"center"} className={'overflow-auto'}>{limitPercentage}</td>
                <td align={"center"} className={'overflow-auto'}>
                    <Button color="link"
                            onClick={this.editEC}
                            disabled={!this.props.isAdmin}>
                        uredi
                    </Button>
                </td>
                <td align={"center"} className={'overflow-auto'}>
                    <Button onClick={this.deleteExpenseCategory}
                            color="danger"
                            disabled={!this.props.isAdmin}>
                        Obriši
                    </Button>
                </td>
            </tr>
        );
    }
}

export default ExpenseCategoriesRow;