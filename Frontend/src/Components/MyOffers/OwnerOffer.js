import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import { Descriptions, Badge, Row, Spin, Checkbox, message, Collapse, Button, Alert, Modal, Input, Rate } from 'antd';
import moment from 'moment';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { blue, green, grey, red } from '@material-ui/core/colors';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn'
import '../../App.css';
import brown from '@material-ui/core/colors/brown';
import FlagIcon from '@material-ui/icons/Flag';
import AccountBalanceIcon from '@material-ui/icons/AccountBalance';
const { Panel } = Collapse;

class Offer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            offer: {},
            userId: sessionStorage.getItem("id"),
            owner: false,
            autoMatches: [],
            displayAutoMatch: false,
            showDualMatches: false,
            loading: false,
            showSplitMatches: false,
            counterModal: false,
            counterAmount: 0
        }
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData = () => {
        this.setState({ loading: true })
        const { match: { params } } = this.props
        const offerId = params.id;
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/" + offerId;
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    offer: response.data,
                    owner: sessionStorage.getItem("id") === response.data.postedBy.id.toString(),
                    loading: false
                })
                console.log(response.data)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    offer: {},
                    loading: false
                })
            });
    }

    fetchAutoMatchOffers = () => {
        this.setState({ loading: true })
        const { match: { params } } = this.props
        const offerId = params.id;

        let url = process.env.REACT_APP_BACKEND_URL + "/offer/automatch/" + offerId;
        axios.defaults.withCredentials = true;
        axios.post(url, {})
            .then(response => {
                this.setState({
                    autoMatches: response.data,
                    loading: false
                })
                console.log(response.data)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    autoMatches: [],
                    loading: false
                })
                message.error("No Auto Matches Found !")
            });
    }

    toggleAutoMatchOffers = () => {
        this.setState({
            displayAutoMatch: !this.state.displayAutoMatch
        }, () => {
            if (this.state.displayAutoMatch) {
                this.fetchAutoMatchOffers();
            }
        })
    }

    toggleDualMatches = () => {
        this.setState({
            showDualMatches: !this.state.showDualMatches
        })
    }

    acceptOffer = (idx, match) => {
        const { match: { params } } = this.props
        const offerId = params.id;
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/automatch/" + offerId + "/equal/process";

        let suggestTotal = 0;
        console.log(idx)
        for (let offer of this.state.autoMatches[idx]["offers"]) {
            console.log(offer.destinationAmount)
            console.log(offer.id)
            suggestTotal += offer.destinationAmount;
        }
        console.log(match)
        console.log(((this.state.autoMatches[idx]["difference"]).toFixed(2) * (1 / this.state.offer.exchangeRate)))
        let amountToAdjust = 0;
        if ((this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2) < this.state.autoMatches[idx]["sum"]) {
            amountToAdjust = this.state.offer.amount + ((this.state.autoMatches[idx]["difference"]) * (1 / this.state.offer.exchangeRate))
        } else {
            amountToAdjust = this.state.offer.amount - ((this.state.autoMatches[idx]["difference"]) * (1 / this.state.offer.exchangeRate))
        }
        let body = {
            "userId": sessionStorage.getItem("id"),
            "offerAmount": this.state.offer.amount,
            "sumOfMatchedOffers": suggestTotal,
            "requestingAmount": match ? this.state.offer.amount : amountToAdjust.toFixed(2),
            "offers": this.state.autoMatches[idx]["offers"]
        }
        console.log(body)
        axios.defaults.withCredentials = true;
        axios.post(url, body)
            .then(response => {
                this.setState({
                    request: response.data
                })
                this.fetchData();
                console.log(response.data)
                message.success("Accepted Offer Successfully")
            })
            .catch((error) => {
                console.log(error);
                message.error("Cannot Accept Offer.");
                this.fetchData();
            });
    }

    onChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }


    counterOffer = () => {
        const idx = this.state.currentOfferId;
        const { match: { params } } = this.props
        const offerId = params.id;
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/automatch/" + offerId + "/unequal/process";

        let suggestTotal = 0;
        for (let offer of this.state.autoMatches[idx]["offers"]) {
            suggestTotal += offer.sourceAmount;
        }
        // let requestingAmount = 0;
        // let counterOffer = this.state.autoMatches[idx]["offers"][this.state.autoMatches[idx]["offers"].length - 1];

        // let counterAmount = (this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2);
        // if (counterAmount > this.state.autoMatches[idx]["sum"]) {
        //     requestingAmount = counterOffer["sourceAmount"] + (counterAmount - this.state.autoMatches[idx]["sum"]);
        // } else {
        //     requestingAmount = counterOffer["sourceAmount"] - (this.state.autoMatches[idx]["sum"] - counterAmount);
        // }
        // if ((requestingAmount < (counterOffer["sourceAmount"] * 0.9)) || (requestingAmount > (counterOffer["sourceAmount"] * 1.1))) {
        //     message.error("Counter Amount Must be in Range");
        //     return;
        // }

        let requestingAmount = this.state.counterAmount;
        let counterOffer = this.state.autoMatches[idx]["offers"][this.state.autoMatches[idx]["offers"].length - 1];

        // let counterAmount = (this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2);

        if (this.state.autoMatches[idx]["type"] === "alpha") {
            requestingAmount = this.state.counterAmount;
        } else if (this.state.autoMatches[idx]["type"] === "beta") {
            if (this.state.counterAmount > this.state.autoMatches[idx]["sum"]) {
                requestingAmount = counterOffer["sourceAmount"] + (this.state.counterAmount - this.state.autoMatches[idx]["sum"]);
            } else {
                requestingAmount = counterOffer["sourceAmount"] - (this.state.autoMatches[idx]["sum"] - this.state.counterAmount);
            }
        } else {
            // const intSum = this.state.autoMatches[idx]["sum"] - this.state.autoMatches[idx]["offers"][1]["destinationAmount"];
            // if (this.state.counterAmount > intSum) {
            //     requestingAmount = counterOffer["sourceAmount"] + (this.state.counterAmount - intSum);
            // } else {
            //     requestingAmount = counterOffer["sourceAmount"] - (intSum - this.state.counterAmount);
            // }
            console.log("gamma")
            console.log(typeof (this.state.counterAmount))
            console.log(typeof (this.state.autoMatches[idx]["offers"][0]["destinationAmount"]))
            const intSum = parseInt(this.state.counterAmount) + parseInt(this.state.autoMatches[idx]["offers"][0]["destinationAmount"]);
            console.log("int sum" + intSum)
            if (intSum > counterOffer["sourceAmount"]) {
                console.log("fi")
                requestingAmount = counterOffer["sourceAmount"] + (intSum - counterOffer["sourceAmount"]);
            } else {
                console.log("else")
                requestingAmount = counterOffer["sourceAmount"] - (counterOffer["sourceAmount"] - intSum);
            }
        }

        if ((requestingAmount < (counterOffer["sourceAmount"] * 0.9)) || (requestingAmount > (counterOffer["sourceAmount"] * 1.1))) {
            console.log(requestingAmount)
            message.error("Counter Amount Must be in Range");
            return;
        }

        let body = {
            "userId": sessionStorage.getItem("id"),
            "offerAmount": (this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2),
            "sumOfMatchedOffers": this.state.autoMatches[idx]["sum"],
            // "adjustmentAmount": requestingAmount.toFixed(2),
            "adjustmentAmount": requestingAmount,
            "offers": this.state.autoMatches[idx]["offers"]
        }
        console.log(requestingAmount)
        console.log(body)
        axios.defaults.withCredentials = true;
        axios.post(url, body)
            .then(response => {
                this.setState({
                    request: response.data
                })
                message.success("Counter Offer Created Successfully")
                this.closeCounterModal();
                console.log(response.data)
            })
            .catch((error) => {
                this.closeCounterModal();
                console.log(error);
                message.error("Cannot Create Counter. Please check your amount");
            });
    }

    openCounterModal = (idx) => {
        this.setState({
            counterModal: true,
            currentOfferId: idx,
            counterAmount: 0
        })
    }

    closeCounterModal = () => {
        this.setState({
            counterModal: false,
            currentOfferId: 0,
            counterAmount: 0
        })
    }

    onChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    render() {
        return (
            <div style={{ marginTop: "50px" }}>
                <Modal
                    title="Enter Your Counter Amount"
                    visible={this.state.counterModal}
                    onOk={this.counterOffer}
                    onCancel={this.closeCounterModal}
                >
                    {/* Do you want to create a Counter of {(this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2)} */}
                    Enter Amount in Destination Currency. Your Requirement at Destination is {(this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2)} {this.state.offer.destinationCurrency}
                    <Input
                        type="number"
                        name="counterAmount"
                        value={this.state.counterAmount}
                        onChange={this.onChange}
                    />
                </Modal>
                <Spin size="large" spinning={this.state.loading}>
                    {this.state.offer ?
                        <div className="mx-32">
                            <Descriptions title="Offer Details" bordered style={{ backgroundColor: "AppWorkspace" }} >
                                <Descriptions.Item label="Id">{this.state.offer.id}</Descriptions.Item>
                                <Descriptions.Item label="Source Currency">{this.state.offer.sourceCurrency}</Descriptions.Item>
                                <Descriptions.Item label="Source Country">{this.state.offer.sourceCountry}</Descriptions.Item>
                                <Descriptions.Item label="Destination Currency">{this.state.offer.destinationCurrency}</Descriptions.Item>
                                <Descriptions.Item label="Destination Country">{this.state.offer.destinationCountry}</Descriptions.Item>
                                <Descriptions.Item label="Exchange Rate">{this.state.offer.exchangeRate}</Descriptions.Item>
                                <Descriptions.Item label="Expiry">{moment(this.state.offer.expiry).format('LLLL')}</Descriptions.Item>
                                <Descriptions.Item label="Amount">{this.state.offer.amount}</Descriptions.Item>
                                <Descriptions.Item label="Status" span={3}>
                                    <Badge status="processing" text={this.state.offer.status} />
                                </Descriptions.Item>
                                {!this.state.owner && this.state.offer.postedBy &&
                                    <Descriptions.Item label="Created By">{this.state.offer.postedBy.nickname}</Descriptions.Item>
                                }
                                {!this.state.owner && this.state.offer.postedBy &&
                                    <Descriptions.Item label="User Rating">
                                        <Link to={"/user/" + this.state.offer.postedBy.id} style={{ cursor: "pointer" }}>
                                            <span>
                                                <Rate defaultValue={this.state.offer.postedBy.rating} disabled />&nbsp;{this.state.offer.postedBy.rating === 0 ? "N/A" : this.state.offer.postedBy.rating}
                                            </span>
                                        </Link>
                                    </Descriptions.Item>
                                }
                            </Descriptions>
                            {this.state.owner && this.state.offer.status === "open" &&
                                <div>
                                    <div style={{ textAlign: "center", fontSize: "20px", fontWeight: "600" }} className="mt-4">
                                        Auto Matching
                                    </div>
                                    <div>
                                        <Checkbox onChange={this.toggleAutoMatchOffers}>Display Auto Matches</Checkbox>
                                        <Checkbox onChange={this.toggleDualMatches}>Display Split Matches</Checkbox>
                                    </div>
                                    {this.state.displayAutoMatch &&
                                        <div style={{ textAlign: "center", alignItems: "center" }} class="container mt-4">
                                            <Alert message={<b> You need {(this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2)} {" " + this.state.offer.destinationCurrency + " in total at your Destination."}</b>} type="warning" />
                                        </div>
                                    }
                                    <div>
                                        {this.state.displayAutoMatch &&
                                            <div>
                                                {this.state.autoMatches &&
                                                    this.state.autoMatches.map((match, index) => {
                                                        if (match.offers.length == 1 || (this.state.showDualMatches)) {
                                                            return (
                                                                <Collapse className="my-4">
                                                                    <Panel header={<div>
                                                                        <b>
                                                                            <div>{"Amount You Get: " + (match.type === "gamma" ? (match.sum - (this.state.offer.amount * this.state.offer.exchangeRate).toFixed(2)) : match.sum) + " " + this.state.offer.destinationCurrency}</div>
                                                                            <div>{" Difference: " + (match.difference).toFixed(2) + " " + this.state.offer.destinationCurrency}</div>
                                                                        </b>
                                                                        <div className="mt-4">
                                                                            {match.difference === 0 && <Button type="primary" onClick={() => this.acceptOffer(index, true)}>Accept</Button>}
                                                                            {match.difference !== 0 && <Button type="primary" onClick={() => this.acceptOffer(index, false)}>Adjust My Offer</Button>}
                                                                            {match.difference !== 0 && match.supportCounter && <Button type="primary" onClick={() => this.openCounterModal(index)} danger className="ml-2">Counter</Button>}
                                                                        </div>
                                                                    </div>} key="1"
                                                                        extra={<div>
                                                                            {/* <Button type="primary" onClick={() => this.acceptRequest(match.offer.id, match.id)}>Accept</Button>
                                                                        <Button type="primary" onClick={() => this.declineRequest(match.offer.id, match.id)} danger className="ml-2">Counter</Button> */}
                                                                        </div>} className="pr-16 ">
                                                                        {match.offers.map((msg, i) => {
                                                                            return (
                                                                                <Card style={{ width: "100%", textAlign: "left" }} className="bg-blue-100 m-8 mr-8">
                                                                                    <CardContent>
                                                                                        <div class="row">
                                                                                            <div class="col-lg-1">
                                                                                                <LocationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                                                                            </div>
                                                                                            <div class="col-lg-2">
                                                                                                <div style={{ marginTop: "10px" }}>Source-<b>{msg.sourceCountry}</b><br></br>Destination-<b>{msg.destinationCountry}</b></div>
                                                                                            </div>
                                                                                            <div class="col-lg-1">
                                                                                                <MonetizationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                                                                            </div>
                                                                                            <div class="col-lg-2">
                                                                                                <div style={{ marginTop: "10px" }}>Source - <b>{msg.sourceCurrency}</b><br></br>Destination - <b>{msg.destinationCurrency}</b></div>
                                                                                            </div>
                                                                                            <div class="col-lg-1">
                                                                                                <AccountBalanceIcon style={{ color: brown[400], fontSize: 60 }} />
                                                                                            </div>
                                                                                            <div class="col-lg-2">
                                                                                                <div style={{ marginTop: "10px" }}>Amount - <b>{msg.sourceAmount + " " + msg.sourceCurrency}</b><br></br>Rate - <b>{msg.exchangeRate}</b></div>
                                                                                            </div>

                                                                                            <div class="col-lg-3">
                                                                                                <div><Badge className="site-badge-count-109" count={msg.status} style={{ backgroundColor: 'teal', fontSize: "15px" }} /></div>
                                                                                                <div>Allow Counter Offers  {msg.supportCounter ? <FlagIcon style={{ color: green[400], fontSize: 20 }} /> : <FlagIcon style={{ color: red[400], fontSize: 20 }} />}</div>
                                                                                                <div style={{}}><b>{msg.nickname}
                                                                                                    <Link to={"/user/" + msg.userId} style={{ cursor: "pointer" }}>
                                                                                                        <span>
                                                                                                            <Rate defaultValue={msg.rating} disabled />&nbsp;{msg.rating === 0 ? "N/A" : msg.rating}
                                                                                                        </span>
                                                                                                    </Link>
                                                                                                </b></div>
                                                                                                <div>Ref# {msg.id}</div>
                                                                                            </div>
                                                                                        </div>
                                                                                        {/* <div class="row">
                                                                                        <PermIdentityIcon style={{ color: brown[400], fontSize: 3 }} /><b>{msg.nickname} (Rating: N/A)</b>
                                                                                    </div> */}
                                                                                        {/* // <div class="row" style={{ marginTop: "20px" }} >
                                                                                        //     <div class="class" style={{ marginLeft: "20PX" }}> Allow Counter Offers  {msg.supportCounter ? <FlagIcon style={{ color: green[400], fontSize: 20 }} /> : <FlagIcon style={{ color: red[400], fontSize: 20 }} />}
                                                                                    //     </div>
                                                                                    // </div>

                                                                                    // <div class="row mt-2">
                                                                                        //     <div class="" style={{ color: green, marginLeft: "20PX" }}> Status <Badge className="site-badge-count-109" count={msg.status} style={{ backgroundColor: 'teal', fontSize: "15px" }} /></div>
                                                                                    // </div> */}
                                                                                    </CardContent>
                                                                                </Card>
                                                                            )
                                                                        })}
                                                                    </Panel>

                                                                </Collapse>
                                                            )
                                                        }
                                                    })
                                                }
                                            </div>
                                        }
                                    </div>
                                </div>
                            }
                        </div>
                        : ""
                    }
                </Spin>
            </div>
        );
    }
}

export default Offer;