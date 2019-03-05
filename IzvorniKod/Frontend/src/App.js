import React, { Component } from 'react';
import { Link, Redirect, Route, BrowserRouter, Switch } from 'react-router-dom';
import { CardGroup, CardTitle, CardText, Card, CardBody, CardHeader } from 'reactstrap';
import './App.css';
import AddUserToGroup from "./components/AddUserToGroup/AddUserToGroup";
import AddUserToSaving from "./components/AddUserToSaving/AddUserToSaving";
import ManageCategoryOfExpenses from "./components/ManageCategoryOfExpenses/ManageCategoryOfExpenses";
import ManageCategoryOfRevenues from "./components/ManageCategoryOfRevenues/ManageCategoryOfRevenues";
import MailVerification from "./components/MailVerification/MailVerification";
import LoginForm from './components/LoginForm/LoginForm';
import RegisterForm from './components/RegisterForm/RegisterForm';
import Invitations from "./components/Invitations/Invitations";
import ManageRevenues from "./components/ManageRevenues/ManageRevenues";
import ManageExpenses from "./components/ManageExpenses/ManageExpenses";
import ManageSavings from "./components/ManageSavings/ManageSavings";
import ManageSavingTransaction from "./components/ManageSavingTransaction/ManageSavingTransaction";
import CreateGroup from "./components/CreateGroup/CreateGroup";
import HomePage from "./components/HomePage/HomePage";
import UserSettings from "./components/UserSettings/UserSettings";
import Header from "./components/Header/Header";
import GroupSettings from "./components/GroupSettings/GroupSettings";
import Savings from "./components/Savings/Savings";
import SavingsForm from "./components/SavingsForm/SavingsForm";


class App extends Component {
  state = {
    authenticated: false,
    loading: true
  };

    componentDidMount() {
        this.setState({ loading: false })

        // fetch('api/users/current', { credentials: 'include' })
        //     .then(response => {
        //         if (response.status !== 404) {
        //             this.setState({ loading: false, authenticated: true })
        //         } else {
        //             this.setState({ loading: false })
        //         }
        //     })
    }

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };


    handleLogin = () => {
        this.setState({ authenticated: true });
    };

      handleLogout = () => {
        this.setState({ authenticated: false });
      };


      loginUser = () => {
        return (
            <LoginForm handleLogin={this.handleLogin}/>
        );
      };

      register = () => {
        return (
            <RegisterForm/>
        );
      };


  render() {

      if (this.state.loading) {
          return <div/>
      }

       // if (!this.state.authenticated) {
       //    return (
       //        <div className="App">
       //            <LoginForm handleLogin={this.handleLogin}/>
       //        </div>
       //    )
       // }

      return (
              <div>
                  {/*<Header />*/}
                  <div className="App">
                      <BrowserRouter>
                          <Switch>
                              <Route path='/addUserToGroup' component={AddUserToGroup} />
                              <Route path='/addUserToSaving' component={AddUserToSaving}/>
                              <Route path='/manageCategoryOfExpenses' component={ManageCategoryOfExpenses}/>
                              <Route path='/manageCategoryOfRevenues' component={ManageCategoryOfRevenues}/>
                              <Route path='/mailVerification' component={MailVerification}/>
                              <Route path='/register' component={RegisterForm}/>
                              <Route path='/invitations' component={Invitations}/>
                              <Route path='/manageRevenues' exact component={ManageRevenues}/>
                              <Route path='/manageExpenses' component={ManageExpenses}/>
                              <Route path='/manageSavings' component={ManageSavings}/>
                              <Route path='/manageSavingTransactions' component={ManageSavingTransaction}/>
                              <Route path='/createGroup' component={CreateGroup}/>
                              <Route path='/homepage' component={HomePage}/>
                              <Route path='/savings' exact component={Savings}/>
                              <Route path='/settings' component={UserSettings}/>
                              <Route path='/groupSettings' component={GroupSettings}/>
                              <Route path='/savings/edit' component={SavingsForm}/>
                              <Route path='/' component={LoginForm}/>
                         </Switch>
                      </BrowserRouter>

                  </div>
              </div>

      );
 }
}

export default App;
