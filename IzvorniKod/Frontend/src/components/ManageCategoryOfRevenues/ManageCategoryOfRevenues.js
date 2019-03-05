import React from "react";
import { Button, Form, FormGroup, Label, Input, Col, Row } from 'reactstrap';
import Header from "../Header/Header";
import "./ManageCategoryOfRevenues.css"

class ManageCategoryOfRevenues extends React.Component {
  state = {
      categoryName: '',
      user: {
          userID: -1
      },
      homeGroup: {
          groupID: -1
      },
      revenueCategory: {
          revenueCategoryID: -1,
          name: '',
          user: null,
          homeGroup: null
      },
      isGroup: false,
      isEdit: false,
      message: ''
  };

    handleChange = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };


    updateCategoryNameField = () => {
        this.setState({categoryName: this.state.revenueCategory.name});
    };

    addNewRevenueCategory = () => {
        const body = {
            name: this.state.categoryName,
            user: this.state.user,
            homeGroup: this.state.homeGroup
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/revenuecategories/', options)
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({message: 'Dodavanje kategorije neuspješno.' });
                } else {
                    if(this.state.homeGroup === null) {
                        this.props.history.push('/settings')
                    } else {
                        this.props.history.push('/groupSettings')
                    }
                    this.setState({message: 'Uspješno dodana kategorija prihoda.'});
                }
            })
    };

    editRevenueCategory = () => {
        const body = {
            revenueCategoryID: this.state.revenueCategory.revenueCategoryID,
            name: this.state.categoryName,
            user: this.state.user,
            homeGroup: this.state.homeGroup
        };
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };
        fetch('api/revenuecategories/' + this.state.revenueCategory.revenueCategoryID, options)
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({message: 'Uređivanje kategorije neuspješno.' });
                } else {
                    if(this.state.homeGroup === null) {
                        this.props.history.push('/settings')
                    } else {
                        this.props.history.push('/groupSettings')
                    }
                    this.setState({message: 'Uspješno uređena kategorija prihoda.'});
                }
            })
    };

  sendData = () => {
      if(this.props.location.state.isEdit) {
          this.editRevenueCategory();
      } else {
          this.addNewRevenueCategory();
      }
  };

    componentDidMount() {
        fetch("api/users/current")
            .then(data => data.json())
            .then(user => this.setState({user: user}))
            .then(() => this.checkIfUserHasGroup(this.state.user.userID));

        if(this.props.location.state.isEdit) {
            this.fetchRevenueCategory(this.props.location.state.revenueCategoryID);
            this.setState({isEdit: true})
        }
    }

    checkIfUserHasGroup = (userID) =>  {
        fetch("api/users/" + userID + "/group")
            .then(data => data.json())
            .then(response => {
                if (response.status >= 400 && response.status < 600) {
                    this.setState({homeGroup: null})
                } else {
                    this.setState({homeGroup: response});
                    this.setState({isGroup: true});
                    this.setState({user: null})
                }
            });
    };

    fetchRevenueCategory = (id) => {
        fetch("api/revenuecategories/" + id)
            .then(response => response.json())
            .then(response => {
                this.setState({revenueCategory: response})
            })
            .then(() =>this.updateCategoryNameField())
    };

    render() {
        return (
            <div >
            <Header/>
            <Row>
                <Col sm="12" md={{ size: 5, offset: 3 }}>
                    <Form className={'mngRC'}>
                        <FormGroup>
                            <Label for="instruction"><h5>Unesite podatke o kategoriji prihoda: </h5></Label>
                        </FormGroup>
                        <FormGroup>
                            <Label for="revenueCategoryName">Ime kategorije prihoda: </Label>
                                <Input name="categoryName"
                                       placeholder="ime kategorije prihoda"
                                       defaultValue={this.state.revenueCategory.name}
                                       onChange={this.handleChange}
                                       value={this.state.categoryName}
                                />
                        </FormGroup>
                        <FormGroup>
                            <Label for="instruction">{this.state.message}</Label>
                        </FormGroup>
                        <Button onClick={() => this.sendData()}>
                            POTVRDI
                        </Button>
                    </Form>
                </Col>
            </Row>
            </div>
        );
    }
}

export default ManageCategoryOfRevenues;
