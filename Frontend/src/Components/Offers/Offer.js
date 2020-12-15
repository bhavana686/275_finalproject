import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Grid from '@material-ui/core/Grid';
import Moment from 'moment';
import { CenterFocusStrong, SystemUpdate } from '@material-ui/icons';
import landingpage from "../Landingpage";
import FormControl from '@material-ui/core/FormControl';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import FormHelperText from '@material-ui/core/FormHelperText';
import { blue, green, grey, red } from '@material-ui/core/colors';
import SvgIcon from '@material-ui/core/SvgIcon';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn'
import MoneyIcon from '@material-ui/icons/Money';
import '../../App.css';
import PermIdentityIcon from '@material-ui/icons/PermIdentity';
import brown from '@material-ui/core/colors/brown';
import FlagIcon from '@material-ui/icons/Flag';
import purple from '@material-ui/core/colors/purple';
import { Badge, Space, Button, Row, message, Modal, Input, Rate, Spin } from 'antd';

const currency = [
    'EUR', 'GBP', 'INR', 'RMB', 'USD'

];

class Offer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            sourceCurrency: "",
            destinationCurrency: "",
            sourceAmount: "",
            destinationAmount: "",
            offers: [],
            edit: false,
            currentOfferId: 0,
            counterAmount: 0,
            loading: false
        }

        this.ChangeHandler = this.ChangeHandler.bind(this);
        this.filterOffer = this.filterOffer.bind(this);
        this.dcHandleChange = this.dcHandleChange.bind(this);
        this.currencyHandleChange = this.currencyHandleChange.bind(this);
        this.validateDetails = this.validateDetails.bind(this);

    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData = () =>{
        this.setState({ loading: true })
        let url = process.env.REACT_APP_BACKEND_URL + '/offers?userId=' + sessionStorage.getItem("id")
        console.log(url);
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    offers: response.data,
                    loading: false
                })
                console.log(this.state.offers)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    offers: [],
                    loading: false
                })
            });;
    }

    filterOffer = (event) => {
        event.preventDefault();
        let url;
        if (this.state.destinationAmount != "" && this.state.sourceAmount != "") {
            url = process.env.REACT_APP_BACKEND_URL + '/offers/filter?destinationAmount=' + this.state.destinationAmount + '&sourceAmount=' + this.state.sourceAmount + '&sourceCurrency=' + this.state.sourceCurrency + '&destinationCurrency=' + this.state.destinationCurrency + "&userId=" + sessionStorage.getItem("id");

        }
        else if (this.state.destinationAmount != "") {
            url = process.env.REACT_APP_BACKEND_URL + '/offers/filter?destinationAmount=' + this.state.destinationAmount + '&sourceCurrency=' + this.state.sourceCurrency + '&destinationCurrency=' + this.state.destinationCurrency + "&userId=" + sessionStorage.getItem("id");

        }
        else {

            url = process.env.REACT_APP_BACKEND_URL + '/offers/filter?sourceAmount=' + this.state.sourceAmount + '&sourceCurrency=' + this.state.sourceCurrency + '&destinationCurrency=' + this.state.destinationCurrency + "&userId=" + sessionStorage.getItem("id");

        }
        axios.defaults.withCredentials = true;
        console.log(url);
        axios.get(url)
            .then(response => {
                console.log(response)
                if (response.status === 200) {
                    this.setState({
                        offers: response.data,
                        flag: true
                    })
                }
                else {
                    this.setState({
                        offers: "",
                        flag: false
                    })

                }
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    offers: "",
                    flag: false
                })
            });;
    }
    dcHandleChange = (event) => {
        this.setState({
            destinationCurrency: event.target.value
        })
    }
    currencyHandleChange = (event) => {
        this.setState({
            sourceCurrency: event.target.value
        })
    }

    ChangeHandler = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        })

    }
    validateDetails = (event) => {
        if (this.state.sourceCurrency !== "" && this.state.destinationCurrency !== "" && (this.state.sourceAmount !== "" || this.state.destinationAmount !== "")) return false
        else return true
    }

    acceptOffer = (id) => {
        this.setState({ loading: true })
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/direct/" + id + "/accept";
        let body = {
            "userId": sessionStorage.getItem("id")
        }
        // message.error("Accept " + id);
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
                // message.error(error);
            });
    }

    counterOffer = () => {
        this.setState({ loading: true })
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/direct/" + this.state.currentOfferId + "/counter";
        let body = {
            "userId": sessionStorage.getItem("id"),
            "amount": this.state.counterAmount
        }
        // message.error("Counter " + this.state.currentOfferId + " with " + this.state.counterAmount);
        axios.defaults.withCredentials = true;
        axios.post(url, body)
            .then(response => {
                this.setState({
                    request: response.data
                })
                this.fetchData();
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

    openCounterModal = (id) => {
        this.setState({
            counterModal: true,
            currentOfferId: id,
            counterAmount: 0
        })
    }

    closeCounterModal = () => {
        this.setState({
            counterModal: false,
            currentOfferId: "",
            counterAmount: 0
        })
    }

    onChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    render() {

        function HomeIcon(props) {
            return (
                <SvgIcon {...props}>
                    <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
                </SvgIcon>
            );
        }
        var displayform = null;
        displayform = this.state.offers ? this.state.offers.map((msg) => {
            return (
                <div>
                    <Modal
                        title="Enter Your Counter Amount"
                        visible={this.state.counterModal}
                        onOk={this.counterOffer}
                        onCancel={this.closeCounterModal}
                    >
                        <Input
                            type="number"
                            name="counterAmount"
                            value={this.state.counterAmount}
                            onChange={this.onChange}
                        />
                    </Modal>
                    <div class="form-group row" >
                        <div class="col-lg-4">
                            <Card style={{ width: "1000px", textAlign: "left" }}>
                                <CardContent>
                                    <div class="row">
                                        <div class="col-lg-1">
                                            <LocationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                        </div>
                                        <div class="col-lg-3">
                                            <div style={{ marginTop: "10px" }}>Source-<b>{msg.sourceCountry}</b><br></br>Destination-<b>{msg.destinationCountry}</b></div>
                                        </div>
                                        <div class="col-lg-1">
                                            <MonetizationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                        </div>
                                        <div class="col-lg-3">
                                            <div style={{ marginTop: "10px" }}>Source-<b>{msg.sourceCurrency}</b><br></br>Destination-<b>{msg.destinationCurrency}</b></div>
                                        </div>
                                        <div class="col-lg-1">
                                            <PermIdentityIcon style={{ color: brown[400], fontSize: 60 }} />
                                        </div>
                                        <div class="col-lg-3">
                                            <div style={{ marginTop: "20px" }}><b>{msg.postedBy ? msg.postedBy.nickname : ""}
                                             <Link to={"/user/" + msg.postedBy.id} style={{ cursor: "pointer" }}>&nbsp;
                                                <span>
                                                    <Rate count={1} defaultValue={msg.postedBy.rating} disabled />&nbsp;{msg.postedBy.rating === 0 ? "N/A" : msg.postedBy.rating}
                                                </span>
                                            </Link>
                                             </b></div>
                                        </div>
                                    </div>
                                    <div class="row" style={{ marginTop: "20px" }} >
                                        <div class="class" style={{ marginLeft: "20PX" }}> Allow Counter Offers  {msg.allowCounterOffers ? <FlagIcon style={{ color: green[400], fontSize: 20 }} /> : <FlagIcon style={{ color: red[400], fontSize: 20 }} />}&nbsp;&nbsp;
                                            Allow Split {msg.allowSplitExchanges ? <FlagIcon style={{ color: green[400], fontSize: 20 }} /> : <FlagIcon style={{ color: red[400], fontSize: 20 }} />}
                                        </div>
                                    </div>
                                    <div class="row" >
                                        <div class="class" style={{ marginLeft: "20PX" }}> <b>{msg.amount + " " + msg.sourceCurrency}</b> at Exchange Rate of <b>{msg.exchangeRate} </b>Ref#: {msg.id}</div>
                                    </div>
                                    <div class="row mt-2">
                                        <div class="" style={{ color: green, marginLeft: "20PX" }}> Status <Badge className="site-badge-count-109" count={msg.status} style={{ backgroundColor: 'teal', fontSize: "15px" }} /></div>
                                    </div>

                                    <Row className="mt-4">
                                        <Button type="primary" onClick={() => this.acceptOffer(msg.id)}>Accept Offer</Button>
                                        <Button type="primary" onClick={() => this.openCounterModal(msg.id)} className="ml-2" danger >Counter Offer</Button>
                                        <Link to={"/offer/" + msg.id}><Button type="primary" ghost className="ml-2">View Offer Details</Button></Link>
                                    </Row>
                                </CardContent>
                            </Card>
                        </div>
                    </div></div>
            )
        }) : "";
        let filterlist = (<div>
            <div>
                <div className="container" >
                    <Grid container >
                        <div style={{ alignContent: "center", width: "100%", marginRight: "20px", paddingBottom: "10px" }}>
                            <Card style={{ height: "100%", marginBottom: "1px" }}>
                                <h4 style={{ paddingLeft: "20px", marginTop: "10px", marginBottom: "4px" }}>SEARCH FILTER</h4>
                                <CardContent>
                                    <form onSubmit={this.filterOffer}>

                                        <div class="form-group row">
                                            <label class="control-label col-sm-9" for="sourceCurrency">Source Currency</label>
                                            <div class="col-sm-3">
                                                <Select

                                                    id="sourceCurrency"
                                                    value={this.state.sourceCurrency}
                                                    onChange={this.currencyHandleChange}
                                                >
                                                    <MenuItem value={currency[0]} >EUR</MenuItem>
                                                    <MenuItem value={currency[1]}>GBP</MenuItem>
                                                    <MenuItem value={currency[2]}>INR</MenuItem>
                                                    <MenuItem value={currency[3]}>RMB</MenuItem>
                                                    <MenuItem value={currency[4]}>USD</MenuItem>

                                                </Select>
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label class="control-label col-sm-9" for="destinationCurrency">Destination Currency</label>
                                            <div class="col-sm-3">
                                                <Select

                                                    id="destinationCurrency"
                                                    value={this.state.destinationCurrency}
                                                    onChange={this.dcHandleChange}
                                                >
                                                    <MenuItem value={currency[0]} >EUR</MenuItem>
                                                    <MenuItem value={currency[1]}>GBP</MenuItem>
                                                    <MenuItem value={currency[2]}>INR</MenuItem>
                                                    <MenuItem value={currency[3]}>RMB</MenuItem>
                                                    <MenuItem value={currency[4]}>USD</MenuItem>

                                                </Select>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-2"></div>
                                            <div class="col-sm-8">
                                                <input type="text" class="form-control" id="nameFilter" aria-describedby="search" placeholder="Source Amount" name="sourceAmount" onChange={this.ChangeHandler} style={{ width: "100%", marginTop: "5px" }} />
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-sm-2"></div>
                                            <div class="col-sm-8">
                                                <input type="text" class="form-control" id="nameFilter" aria-describedby="search" placeholder="Destination Amount" name="destinationAmount" onChange={this.ChangeHandler} style={{ width: "100%", marginTop: "5px" }} />
                                            </div>
                                        </div>
                                        <button onClick={this.submitEvent} class="btn btn-primary" type="submit" disabled={this.validateDetails()}>GO</button>&nbsp;
                                            </form>
                                </CardContent>
                            </Card>
                        </div> </Grid>
                </div>

            </div>

        </div>)





        return (
            <div style={{ marginTop: "30px" }}>
                <Spin size="large" spinning={this.state.loading}>
                <div class='row' >
                    <div class='col-md-3' >
                        <div>{filterlist}</div>
                    </div>
                    <div class='col-md-7' style={{ marginTop: "" }}>
                        <div class="grid-container">
                            {displayform}
                        </div>

                    </div>
                </div>

</Spin>
            </div>
        );
    }
}





export default Offer;