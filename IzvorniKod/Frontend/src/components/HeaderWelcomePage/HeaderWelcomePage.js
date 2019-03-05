import {Component} from "react";
import {Navbar, NavbarBrand, NavbarToggler} from "reactstrap";
import React from "react";

class HeaderWelcomePage extends Component {

    render() {
        return (
            <div className="navBar">
                <Navbar color={"dark"} dark expand="md">
                    <NavbarBrand   className="font-weight-bold text-white align-items-center">
                        Praćenje troškova
                    </NavbarBrand>
                </Navbar>
            </div>
        );
    }

}
export default HeaderWelcomePage;