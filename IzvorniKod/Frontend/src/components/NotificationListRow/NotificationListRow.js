import React, { Component } from 'react';
import {Button} from "reactstrap";
import './NotificationListRow.css'
import dateFormat from "dateformat";

class NotificationListRow extends Component {

    constructor(props) {
        super(props);
    }


    render() {
        let { notificationMessage,notificationTime } = this.props.notification;
        return (
                <tr className={'red'}>
                    <td align={"center"}
                        className={'overflow-auto' }
                        >{dateFormat(notificationTime,'dd.mm.yyyy.')}</td>
                    <td align={"center"}
                        className={'overflow-auto'}>{notificationMessage}</td>
                </tr>
        );
    }
}

export default NotificationListRow;