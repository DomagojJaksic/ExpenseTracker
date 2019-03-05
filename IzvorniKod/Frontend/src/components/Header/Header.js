import React, { Component } from 'react';
import { Link } from "react-router-dom";
import './Header.css';
import {Button,Collapse, Navbar,NavbarToggler, NavbarBrand, Nav, NavItem, NavLink} from "reactstrap";
import logo from '../../images/homeIcon.png'

class Header extends Component {

    constructor(props) {
        super(props);

        this.toggle = this.toggle.bind(this);
        this.state = {
            isOpen: false
        };
    }
    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    logout = () => {
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        };
        fetch('api/logout', options)
            .then(response => {
                if (response.status === 401) {
                    this.setState({ loginFail: true})
                } else {

                }
            })
    };

    navSettings = () => {
        this.props.history.push("/settings");
    };
    
    render() {
        return (
            <div className="navBar">
                <Navbar color={"dark"}  dark expand="md" >
                    <NavbarToggler onClick={this.toggle} />
                    <NavbarBrand className="font-weight-bold text-white"  >
                            Praćenje troškova
                    </NavbarBrand>
                    <NavbarBrand style={{ width: 50, height: 50}}
                                 className=""
                    ><Link to = "/homepage">
                        <img src={logo} width="30" height="30" /></Link>
                    </NavbarBrand>
                    <Collapse isOpen={this.state.isOpen} navbar>
                        <Nav className="ml-auto" navbar>
                                <NavItem>
                                    <Link to="/groupSettings" className={"nav-link"}>Grupa</Link>
                                </NavItem>
                                <NavItem>
                                    <Link to="/settings" className={"nav-link"}>Korisničke postavke</Link>
                                </NavItem>
                                <NavItem>
                                    <Link to='/invitations' className={"nav-link"}>Pozivnice</Link>
                                </NavItem>
                                <NavItem>
                                    <Link to="/" onClick={this.logout} className = "nav-link">Odjava</Link>
                                </NavItem>
                        </Nav>
                    </Collapse>
                </Navbar>

            </div>
        )
    }
}

export default Header;
