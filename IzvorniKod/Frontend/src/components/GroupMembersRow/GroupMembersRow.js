import React, { Component } from 'react';
import {Button} from "reactstrap";
import Switch from "@material-ui/core/Switch/Switch";

class GroupMembersRow extends Component {

    state = {
        isUserAdmin: false,
        checked: false
    };

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
        if(this.props.groupID > 0) {
            this.props.setInfo(this.props.groupID, this.props.user.userID)
        } else {
            this.props.setInfo(this.props.savingID, this.props.user.userID)
        }
    };

    handleChangePronounce = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
        if(this.props.groupID > 0) {
            this.pronounceAdminGroup(this.props.member.username, this.props.groupID);
        } else {
            this.pronounceAdminSaving(this.props.member.username, this.props.savingID);
        }
    };

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.setState({isUserAdmin: this.props.isUserAdmin});
        if(this.props.groupID > 0) {
            this.checkIfUserIsGroupAdmin(this.props.groupID, this.props.member.userID);
            this.checkIfCurrentUserIsGroupAdmin(this.props.user.username, this.props.groupID);
        } else {
            this.checkIfUserIsSavingAdmin(this.props.savingID, this.props.member.userID);
            this.checkIfCurrentUserIsSavingAdmin(this.props.user.username, this.props.savingID);
        }
    }

    checkIfUserIsGroupAdmin = (id, userID) => {
        fetch('api/groups/' + id + '/admins')
            .then(response => response.json())
            .then(response => {
                const admins = response;
                for(let i = 0; i < admins.length; i++) {
                    if(admins[i].userID === userID) {
                        this.setState({checked: true})
                    }
                }
            })
    };

    checkIfUserIsSavingAdmin = (id, userID) => {
        fetch('api/savings/' + id + '/admins')
            .then(response => response.json())
            .then(response => {
                const admins = response;
                for(let i = 0; i < admins.length; i++) {
                    if(admins[i].userID === userID) {
                        this.setState({checked: true});
                        this.setState({isUserAdmin: true});
                    }
                }
            })
    };

    kickUserFromGroup = () => {
        const body = {
            id: this.props.groupID,
            username: this.props.member.username
        };
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/groupmembers/', options)
            .then(response => {
                this.props.setInfo(this.props.groupID, this.props.user.userID);
                if(response.ok) {
                    if(this.props.member.userID === this.props.user.userID) {
                        this.props.navigateHP();
                    }
                }
            })
    };

    kickUserFromSaving = () => {
        const body = {
            id: this.props.savingID,
            username: this.props.member.username
        };
        const options = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/savingmembers/', options)
            .then(response => {
                this.props.setInfo(this.props.savingID, this.props.user.userID);
                if(response.ok) {
                    if(this.props.member.userID === this.props.user.userID) {
                        this.props.navigateHP();
                    }
                }
            })
    };

    kickUser = () => {
        if(this.props.groupID > 0) {
            this.kickUserFromGroup();
        } else {
            this.kickUserFromSaving()
        }
    };

    pronounceAdminGroup = (username, id) => {
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/groupmembers/' + username + '/' + id + '/adminstatus', options)
            .then(response => {
                if(this.props.isUserAdmin) {
                    this.setState({checked: !this.state.checked});
                    if(username === this.props.user.username) {
                        this.setState({isUserAdmin: this.state.checked});
                        this.props.setInfo(this.props.groupID, this.props.user.userID)
                    }
                }
                if(response.ok && username === this.props.user.username) {
                    this.props.navigateHP();
                }
            })
    };

    pronounceAdminSaving = (username, id) => {
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/savingmembers/' + username + '/' + id + '/adminstatus', options)
            .then(response => {
                if(this.props.isUserAdmin) {
                    this.setState({checked: !this.state.checked});
                    if(username === this.props.user.username) {
                        this.setState({isUserAdmin: this.state.checked});
                        this.props.setInfo(this.props.savingID, this.props.user.userID)
                    }
                }
                if(response.ok && username === this.props.user.username) {
                    this.props.navigateHP();
                }
            })
    };

    checkIfCurrentUserIsGroupAdmin = (username, groupID) => {
        fetch('api/groups/' + groupID + '/isAdmin/' + username)
            .then(response => {
                if(response.ok) {
                    this.setState({isUserAdmin: true})
                } else {
                    this.setState({isUserAdmin: false})
                }
            })
    };

    checkIfCurrentUserIsSavingAdmin = (username, savingID) => {
        fetch('api/savings/' + savingID + '/isAdmin/' + username)
            .then(response => {
                if(response.ok) {
                    this.setState({isUserAdmin: true})
                } else {
                    this.setState({isUserAdmin: false})
                }
            })
    };

    render() {
        let { username } = this.props.member;
        return (
            <tr>
                <td className={'overflow-auto'} align={"center"}>{username}</td>
                <td className={'overflow-auto'} align={"center"}>
                    <Button onClick={this.kickUser}
                            disabled={!this.props.isUserAdmin}
                            color="danger">Obriši
                    </Button>
                </td>
                <td className={'overflow-auto'} align="center">
                    <Switch
                        checked={this.state.checked}
                        onChange={this.handleChangePronounce}
                        value="checked"
                        color="primary"
                        disabled={!this.props.isUserAdmin}
                    />
                </td>
            </tr>
        );
    }
}

export default GroupMembersRow;