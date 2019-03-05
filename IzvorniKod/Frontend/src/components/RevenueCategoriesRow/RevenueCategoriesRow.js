import React, { Component } from 'react';
import {Button} from "reactstrap";


class RevenueCategoriesRow extends Component {

    constructor(props) {
        super(props);
    }

    deleteRevenueCategory = () => {
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/revenuecategories/' + this.props.revenueCategory.revenueCategoryID, options)
            .then(response => {
                if(this.props.groupID > 0) {
                    this.props.setInfo(this.props.groupID, this.props.user.userID);
                } else {
                    this.props.updateComponent(this.props.user.userID)
                }
            })
    };

    editRC = () => {
        this.props.editRevenueCategory(this.props.revenueCategory.revenueCategoryID);
    };

    render() {
        let { name } = this.props.revenueCategory;
        return (
            <tr>
                <td className={'overflow-auto'} align={"center"}>{name}</td>
                <td className={'overflow-auto'} align={"center"}>
                    <Button onClick={this.editRC}
                            color="link"
                            disabled={!this.props.isAdmin}>
                        uredi
                    </Button>
                </td>
                <td className={'overflow-auto'} align={"center"}>
                    <Button onClick={this.deleteRevenueCategory}
                            color="danger"
                            disabled={!this.props.isAdmin}>
                        Obriši
                    </Button>
                </td>

            </tr>
        );
    }
}

export default RevenueCategoriesRow;