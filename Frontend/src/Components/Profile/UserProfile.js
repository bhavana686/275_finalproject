import React, { Component } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import moment from 'moment';
import { Descriptions, Badge, Row, Spin, Checkbox, message, Collapse, Button, Alert, Modal, Input, Rate } from 'antd';
import { blue, green, grey, red } from '@material-ui/core/colors';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn';
import brown from '@material-ui/core/colors/brown';
import FlagIcon from '@material-ui/icons/Flag';
import AccountBalanceIcon from '@material-ui/icons/AccountBalance';
const { Panel } = Collapse;

class UserProfile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: {},
            mailSent: false,
            msgText: "",
        }
        this.ChangeHandler = this.ChangeHandler.bind(this);
        this.submitEvent = this.submitEvent.bind(this);
    }

    componentDidMount() {
        const { match: { params } } = this.props
        const userId = params.id;
        let url = process.env.REACT_APP_BACKEND_URL + '/user/' + userId;
        console.log(url);
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    user: response.data
                })
                console.log(this.state.useracc)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    user: ""
                })
            });;
    }
    submitEvent = () => {
        let url = process.env.REACT_APP_BACKEND_URL + '/offers/email';
        var data = {
            "sendto": this.state.user.username,
            "subject": "New Message From " + sessionStorage.getItem("username"),
            "message": this.state.msgText
        }
        axios.post(url, data)
            .then(response => {
                if (response.status == 200) {
                    this.setState({
                        mailSent: true,
                    })
                    message.success("Message Sent Successfully")
                }
                else {
                    this.setState({
                        mailSent: false,
                    })
                }
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    mailSent: false,
                })
            });
    }

    ChangeHandler = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        })
    }
    render() {
        var displayform = null;
        displayform = (
            <div>
                <div class="form-group row" >
                    <div class="col-lg-4"></div>
                    <div class="col-lg-4">
                        <Card style={{ height: "100px", width: "500px", textAlign: "left" }}>
                            <CardContent>
                                <div class="row">
                                    <div class="col-lg-6"> User Name</div>
                                    <div class="col-lg-6">  {this.state.user && this.state.user.username}</div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-6">  Nick name</div>
                                    <div class="col-lg-6">  {this.state.user && this.state.user.nickname}</div>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                </div>
            </div>
        )
        return (
            <div style={{ marginTop: "50px" }}>
                <div style={{ textAlign: "center", fontSize: "20px", fontWeight: "600" }} >
                    User Details
                    </div>
                {displayform}
                <div>
                    <div class="container" >
                        <div class="row">
                            <div class="col-lg-10 col-xl-9 mx-auto">
                                <div class="card card-signin flex-row my-5">
                                    <div class="form-label-group">
                                        <div class="col-sm-9" style={{ height: "100%", width: "100%" }}>
                                            <textarea rows={4} cols={70} name="msgText" id="msgText" placeHolder=" Enter your Message" onChange={this.ChangeHandler} class="form-control" required />
                                        </div>
                                    </div><br /><br />
                                    <div class="form-group mt-4">
                                        <div class="col-sm-10">
                                            <button onClick={this.submitEvent} class="btn btn-primary" type="submit">Send Message</button>&nbsp;
                                            <Link to="/"><button type="submit" class="btn btn-primary">Cancel</button></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="mt-16 mx-32">
                    <div style={{ textAlign: "center", fontSize: "20px", fontWeight: "600" }} className="mt-4">
                        Transactions Of {this.state.user && this.state.user.nickname} ({this.state.user.rating === 0 ? "N/A" : this.state.user.rating})
                    </div>
                    {this.state.user && this.state.user.transactions &&
                        this.state.user.transactions.map(transaction => {
                            return (
                                <div>
                                    <Collapse className="my-4">
                                        <Panel header={<div>
                                            <b>
                                                <div>{"Status: " + transaction.status + ". Date: " + moment(transaction.expiry).format("LLLL")}</div>
                                            </b>
                                        </div>} key="1" >
                                            <div>
                                                <Card style={{ width: "100%", textAlign: "left" }} className="bg-blue-100 m-8 mr-8">
                                                    <CardContent>
                                                        <div class="row">
                                                            <div class="col-lg-1">
                                                                <LocationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                                            </div>
                                                            <div class="col-lg-2">
                                                                <div style={{ marginTop: "10px" }}>Source-<b>{transaction.offer.sourceCountry}</b><br></br>Destination-<b>{transaction.offer.destinationCountry}</b></div>
                                                            </div>
                                                            <div class="col-lg-1">
                                                                <MonetizationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                                            </div>
                                                            <div class="col-lg-2">
                                                                <div style={{ marginTop: "10px" }}>Source - <b>{transaction.offer.sourceCurrency}</b><br></br>Destination - <b>{transaction.offer.destinationCurrency}</b></div>
                                                            </div>
                                                            <div class="col-lg-1">
                                                                <AccountBalanceIcon style={{ color: brown[400], fontSize: 60 }} />
                                                            </div>
                                                            <div class="col-lg-2">
                                                                <div style={{ marginTop: "10px" }}>Amount - <b>{transaction.amountAdjusted + " " + transaction.offer.sourceCurrency}</b><br></br></div>
                                                            </div>
                                                            <div class="col-lg-3">
                                                                <div>Ref# {transaction.offer.id}</div>
                                                            </div>
                                                        </div>
                                                    </CardContent>
                                                </Card>
                                                <div style={{ textAlign: "center", fontSize: "20px", fontWeight: "600" }} className="mt-4">
                                                    Other Transactions
                                                </div>
                                                {transaction.otherTransactions && transaction.otherTransactions.map(other => {
                                                    return (
                                                        <Card style={{ width: "100%", textAlign: "left" }} className="bg-blue-100 m-8 mr-8">
                                                            <CardContent>
                                                                <div class="row">
                                                                    <div class="col-lg-1">
                                                                        <LocationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                                                    </div>
                                                                    <div class="col-lg-2">
                                                                        <div style={{ marginTop: "10px" }}>Source-<b>{other.offer.sourceCountry}</b><br></br>Destination-<b>{other.offer.destinationCountry}</b></div>
                                                                    </div>
                                                                    <div class="col-lg-1">
                                                                        <MonetizationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                                                    </div>
                                                                    <div class="col-lg-2">
                                                                        <div style={{ marginTop: "10px" }}>Source - <b>{other.offer.sourceCurrency}</b><br></br>Destination - <b>{other.offer.destinationCurrency}</b></div>
                                                                    </div>
                                                                    <div class="col-lg-1">
                                                                        <AccountBalanceIcon style={{ color: brown[400], fontSize: 60 }} />
                                                                    </div>
                                                                    <div class="col-lg-2">
                                                                        <div style={{ marginTop: "10px" }}>Amount - <b>{other.amountAdjusted.toFixed(2) + " " + other.offer.sourceCurrency}</b><br></br></div>
                                                                    </div>
                                                                    <div class="col-lg-3">
                                                                        <div>Ref# {other.offer.id}</div>
                                                                        <div>User - <b>{other.user.nickname.substring(0, 2)}****
                                                                        <Link to={"/user/" + other.user.id} style={{ cursor: "pointer" }}>
                                                                                <span>
                                                                                    <Rate defaultValue={other.user.rating} disabled />&nbsp;{other.user.rating === 0 ? "N/A" : other.user.rating}
                                                                                </span>
                                                                            </Link>
                                                                        </b></div>
                                                                    </div>
                                                                </div>
                                                            </CardContent>
                                                        </Card>
                                                    )
                                                })}
                                            </div>

                                        </Panel>
                                    </Collapse>
                                </div>
                            )
                        })
                    }
                </div>
            </div>
        );
    }
}





export default UserProfile;