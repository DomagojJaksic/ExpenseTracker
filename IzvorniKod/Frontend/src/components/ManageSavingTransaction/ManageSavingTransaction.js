import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { Button, Form, FormGroup, Label, Input, FormText } from 'reactstrap';
import './ManageSavingTransaction.css';
import Header from "../Header/Header";


class ManageSavingTransaction extends Component {

	state = {
		amount: 0.,
		type: '',
        error: '',
        user: null,
        saving: null,
        isEdit: false,
        savingTransactionID: -1,
        time: null,
        info: ''
	};


  	handleChange = (event) => {
		this.setState({
			[event.target.name]: event.target.value
		});
	};

    addSavingTransaction = () => {
        const body = {
            user: this.state.user,
            saving: this.state.saving,
            amount: this.state.amount,
            type: this.state.type
        };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };

        fetch('api/transactions/', options)
            .then(response => {
                if(response.status >= 400 && response.status < 600) {
                    if(this.state.type === 'WITHDRAW') {
                        this.setState({info: 'Nedovoljna količina novca za ovu isplatu.'})
                    } else {
                        this.setState({info: 'Dodavanje transakcije štednje neuspješno.'})
                    }
                } else {
                    this.setState({info: 'Dodavanje transakcije štednje uspješno.'})
                    this.redirectToSavings(this.props.location.state.savingID)
                }
            })
    };

    editSavingTransaction = () => {
        const body = {
            savingTransactionID: this.state.savingTransactionID,
            user: this.state.user,
            saving: this.state.saving,
            amount: this.state.amount,
            type: this.state.type,
            time: this.state.time
        };
        const options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        };

        fetch('api/transactions/' + this.state.savingTransactionID, options)
            .then(response => {
                if(response.status >= 400 && response.status < 600) {
                    this.setState({info: 'Uređivanje transakcije štednje neuspješno.'})
                } else {
                    this.setState({info: 'Uređivanje transakcije štednje uspješno.'})
                    this.redirectToSavings(this.props.location.state.savingID)
                }
            })
    };

    fetchCurrentUser = () => {
        fetch('api/users/current')
            .then(response => response.json())
            .then(user => this.setState({user: user}));
    };

    fetchSaving = (id) => {
        fetch('api/savings/' + id)
            .then(response => response.json())
            .then(saving => this.setState({saving: saving}));
    };

    fetchSavingTransaction = (id) => {
        fetch('api/transactions/' + id)
            .then(response => response.json())
            .then(savingTransaction =>  {
                this.setState({savingTransactionID: savingTransaction.savingTransactionID});
                this.setState({amount: savingTransaction.amount});
                this.setState({type: savingTransaction.type});
                this.setState({user: savingTransaction.user});
                this.setState({saving: savingTransaction.saving});
                this.setState({time: savingTransaction.time});

            });
    };

    sendData = () => {
      if(this.props.location.state.isEdit) {
          this.editSavingTransaction();
      } else {
          this.addSavingTransaction();
      }
    };

    redirectToSavings = (savingID) => {
        this.props.history.push({
            pathname: '/savings',
            state:{
                savingID:savingID

            }
        })
    };

    componentDidMount() {
        if(this.props.location.state.isEdit) {
            this.setState({isEdit: true});
            this.fetchSavingTransaction(this.props.location.state.savingTransactionID);
        }
        this.fetchCurrentUser();
        this.fetchSaving(this.props.location.state.savingID);
    }

    render() {

        return (
            <div className="ManageSavingTransaction">
                <Header/>
                <Col sm="12" md={{ size: 6, offset: 3 }}>
                    <Container className={"mngST"}>
                        <Form>
                            <Row>
                                <Col sm="12" md={{ size: 8, offset: 2 }}>
                                    <Label for="info"><h4>Unesite podatke o transakciji štednje</h4></Label>
                                </Col>
                            </Row>
                            <Row>
                                 <Col sm="12" md={{ size: 7, offset: 2 }}>
                                    <FormGroup>
                                       <Label for="amountLabel">Iznos:</Label>

                                       <Input
                                            type="number"
                                            name="amount"
                                            id="amount"
                                            placeholder="0.00"
                                            onChange={this.handleChange}
                                            value={this.state.amount}
                                       />
                                    </FormGroup>

                                    <FormGroup>
                                         <Label for="transactionType">Vrsta transakcije:</Label>

                                        <Input  type="select"
                                                name="type"
                                                onChange={this.handleChange}
                                                value={this.state.type}
                                        >
                                            <option>Odaberite vrstu transakcije</option>
                                            <option value = "DEPOSIT">Uplata</option>
                                            <option value = "WITHDRAW">Isplata</option>
                                        </Input>
                                    </FormGroup>

                                    <Button color='secondary'
                                            onClick={() => this.sendData()}>
                                        POTVRDI
                                    </Button>

                                    <Row>
                                         <Col sm="12" md={{ size: 7, offset: 3 }}>
                                            <Label>
                                                {this.state.info}
                                            </Label>
                                         </Col>
                                    </Row>
                                </Col>
                            </Row>
                        </Form>
                    </Container>
                </Col>
            </div>
        );
    }
}

export default ManageSavingTransaction;