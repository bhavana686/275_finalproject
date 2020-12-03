import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
// import { Card, Button, ButtonGroup } from 'react-bootstrap';
import { blue, green, grey, red } from '@material-ui/core/colors';
import Moment from 'moment';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Grid from '@material-ui/core/Grid';
import PermIdentityIcon from '@material-ui/icons/PermIdentity';
import brown from '@material-ui/core/colors/brown';
import FlagIcon from '@material-ui/icons/Flag';
import { Badge, Space, Button, Row, message, Modal, Input } from 'antd';
import SvgIcon from '@material-ui/core/SvgIcon';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn'

class Offers extends Component {
    constructor(props) {
        super(props);
        this.state = {
            sourceCurrency: "",
            targetCurrency: "",
            exchangeRate: "",
            offers: [],
            edit: false,
            id: sessionStorage.getItem("id")
        }
        this.handleUpdate = this.handleUpdate.bind(this);
        this.ChangeHandler = this.ChangeHandler.bind(this);

    }

    handleUpdate(id) {
        console.log("in here")
        this.setState({
            redirect: `/editoffer/${id}`
        })
    }


    componentDidMount() {
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/user/" + this.state.id;
        console.log(url);
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    offers: response.data
                })
                console.log(this.state.offers)

            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    offers: []
                })
            });;
    }


    ChangeHandler = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        })
    }
    render() {
        let redirectVar = null
        if (this.state.redirect) {
            redirectVar = <Redirect push to={this.state.redirect} />;
        }
        let editform = null;
        let details = null;
        var displayform = null;



        displayform = this.state.offers.map((msg) => {
            return (
                <div>
                    &nbsp;
                    
                        {/* <Card style={{ width: "50%", marginLeft: "100px", height: "80%", backgroundColor: "white" }}>
                            <Card.Body>
                                <Card.Text>
                                    <p style={{ color: "black" }}>Source Currency: {msg.sourceCurrency}</p>
                                    <p style={{ color: "black" }}>Destination Currency : {msg.destinationCurrency}</p>
                                    <p style={{ color: "black" }}>Exchange Rate : {msg.exchangeRate}</p>
                                    <p style={{ color: "black" }}>Amount : {msg.amount}</p>
                                    <p style={{ color: "black" }}>Status : {msg.status}</p>
                                    <p style={{ color: "black" }}>Expiry Date :  {Moment(msg.expiry).format('YYYY-MM-DD')}</p>
                                    <span hidden={!msg.editable}>
                                        <div><Button onClick={(e) => this.handleUpdate(msg.id)}>Edit Offer</Button></div></span>
                                </Card.Text>
                            </Card.Body>
                        </Card> */}
                        <Card style={{ textAlign: "left" }} >
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
                                        <div style={{ marginTop: "20px" }}><b>{msg.postedBy ? msg.postedBy.nickname : ""} (Rating: N/A)</b></div>
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

                                <Row className="mt-4 items-center">
                                <Link to={"/offer/" + msg.id}><Button type="primary">Go To Offer Details</Button></Link>&nbsp;
                                

                                
                                <Link to={"/editoffer/" + msg.id}><Button type="primary" disabled={!msg.editable}>Edit Offer</Button></Link>
                                </Row>
                            </CardContent>
                        </Card>
                </div>
            )
        })

        return (
            <div style={{ marginTop: "50px" }} className="mx-32">
                <div style={{ textAlign:"right" }}>
                    {redirectVar}
                    <Link to="/createoffer"><button type="submit" class="btn btn-primary">Post A New Offer</button></Link>

                </div>
                {displayform}
            </div>
        );
    }
}





export default Offers;