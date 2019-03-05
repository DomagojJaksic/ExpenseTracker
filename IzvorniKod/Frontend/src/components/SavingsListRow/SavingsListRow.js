import React, { Component } from 'react';
import {Button} from "reactstrap";
import './SavingsListRow.css';
import dateFormat from "dateformat";


class SavingsListRow extends Component {

    state= {
        startDate: '',
        endDate: '',
        currentBalance: 0,
        targetedAmount: 0
    };

    constructor(props) {
        super(props);
    }

    updateSaving = () => {
      fetch('api/savings/' + this.props.saving.savingID)
          .then(response => response.json())
          .then(response => {
              this.setState({currentBalance: response.currentBalance});
              this.setState({targetedAmount: response.targetedAmount});
              this.setState({startDate: response.startDate});
              this.setState({endDate: response.endDate});

          })
    };

    render() {
        let { startDate, endDate, name,currentBalance, targetedAmount } = this.props.saving;

        return (
                <tr className={'red'}>
                    <td align={"center"}
                        className={'overflow-auto'}>{dateFormat(startDate,'dd.mm.yyyy.')}</td>
                    <td align={"center"}
                        className={'overflow-auto'}>{dateFormat(endDate,'dd.mm.yyyy.')}</td>
                    <td align={"center"}
                        className={'overflow-auto'}>{currentBalance}</td>
                    <td align={"center"}
                        className={'overflow-auto'}>{targetedAmount}</td>
                    <td align={"center"}
                        className={'overflow-auto'}>
                        <Button color="link"
                                onClick={() => this.props.redirectToSavings(this.props.saving.savingID)}
                        >
                            {name}
                        </Button>
                    </td>
                </tr>
        );
    }
}

export default SavingsListRow;