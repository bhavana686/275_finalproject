import React, { Component, Fragment } from "react";
import axios from "axios";
import { Redirect } from "react-router";
import { Button } from "react-bootstrap";
import Icon from "@material-ui/core/Icon";
import '../Offers/editoffer.css';
import Moment from 'moment';
import TextField from '@material-ui/core/TextField';



class CreateOffer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      allowCounterOffers: "",
      allowSplitExchanges: "",
      amount: "",
      counter: "",
      destinationCountry: "",
      destinationCurrency: 0,
      editable: 0,
      exchangeRate: 0,
      expiry: "",
      fullyFulfilled: "",
      id: 0,
      sourceCountry: 0,
      sourceCurrency: "",
      status: "",
      transactedAmount: "",
      usePrevailingRate: "",
      userid: sessionStorage.getItem("id")
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleAdd = this.handleAdd.bind(this);
  }


  handleChange = (e) => {
    console.log("e", e.target.name, " ", e.target.value);

    this.setState({
      [e.target.name]: e.target.value,
    });
  };

  handleAdd = (e) => {
    e.preventDefault();
    const data = {
        allowCounterOffers: this.state.allowCounterOffers,
        allowSplitExchanges: this.state.allowSplitExchanges,
        amount: this.state.amount,
        counter: this.state.counter,
        destinationCountry: this.state.destinationCountry,
        destinationCurrency: this.state.destinationCurrency,
        editable: this.state.editable,
        exchangeRate: this.state.exchangeRate,
        expiry:this.state.expiry,
        fullyFulfilled: this.state.fullyFulfilled,
        sourceCountry: this.state.sourceCountry,
        sourceCurrency: this.state.sourceCurrency,
        status: this.state.status,
        transactedAmount: this.state.transactedAmount,
        userid : this.state.userid,
        usePrevailingRate: this.state.usePrevailingRate,
   
    };
  let userid = data.userid
  console.log(data.sourceCountry)
    // let url = process.env.REACT_APP_BACKEND_URL+'/offer?sourceCountry='+data.sourceCountry+'&sourceCurrency='+data.sourceCurrency+'&destinationCountry='+this.state.destinationCountry+'&destinationCurrency='+this.state.destinationCurrency;
let url = process.env.REACT_APP_BACKEND_URL+'/offer/postoffer/'
    //set the with credentials to true
    axios.defaults.withCredentials = true;
    //make a post request with the user data
    console.log("req.body", data);
    axios
      .post(url, data)
      .then((response) => {
        console.log(response);
        if (response.data === "error") {
          console.log("error");
          this.setState({
            showRegistrationError: true,
          });
        } else if (response.data === "Successfully updated the offer") {
          this.setState({
            redirect: `/offers/`,
          });
        }
      })
      .catch((ex) => {
        this.setState({
          showRegistrationError: true,
        });
      });
  };

  render() {
    let redirectVar = null;
    if (this.state.redirect) {
      redirectVar = <Redirect push to={this.state.redirect} />;
    }

    return (
      <div className="container contact-form">
        {redirectVar}
        <div className="contact-image">
          <img
            src="https://image.ibb.co/kUagtU/rocket_contact.png"
            alt="rocket_contact"
          />
        </div>
        <form method="post">
          <h3>Update Offer here!</h3>
          {/* {msgshow} */}
          <div className="row">
            <div className="col-md-6">
              
            <div className="form-group">
            <label className="col-form-label w-100 text-left">
             Select Destination Country
            </label>&nbsp;&nbsp;&nbsp;
                <select
                  name="destinationCountry"
                  className="custom-select custom-select-sm"
                  onChange={this.handleChange}
                  value={this.state.destinationCountry}
                >
                 
                 <option value={""}>Destination Country</option>
                  <option value={"Europe"}>Europe</option>
                  <option value={"UK"}>UK</option>
                  <option value={"India"}>India</option>
                  <option value={"China"}>China</option>
                  <option value={"US"}>US</option>
                </select>
              </div>

              <div className="form-group">
            <label className="col-form-label w-100 text-left">
            Select Destination Currency
            </label>
                <select
                  name="destinationCurrency"
                  className="custom-select custom-select-sm"
                  onChange={this.handleChange}
                  value={this.state.destinationCurrency}
                >
               
                  <option value={""}>Destination Currency</option>
                  <option value={"EUR"}>EUR</option>
                  <option value={"GBP"}>GBP</option>
                  <option value={"INR"}>INR</option>
                  <option value={"RMB"}>RMB</option>
                  <option value={"USD"}>USD</option>
                </select>
              </div>

              <div className="form-group">
            <label className="col-form-label w-100 text-left">
             Seclect Source Country
            </label>
                <select
                  name="sourceCountry"
                  className="custom-select custom-select-sm"
                  onChange={this.handleChange}
                  value={this.state.sourceCountry}
                >
                  <option value={""}>Source Country</option>
                  <option value={"Europe"}>Europe</option>
                  <option value={"UK"}>UK</option>
                  <option value={"India"}>India</option>
                  <option value={"China"}>China</option>
                  <option value={"US"}>US</option>
                </select>
              </div>

              <div className="form-group">
            <label className="col-form-label w-100 text-left">
            Select Source Currency
            </label>
                <select
                  name="sourceCurrency"
                  className="custom-select custom-select-sm"
                  onChange={this.handleChange}
                  value={this.state.sourceCurrency}
                >
               
                  <option value={""}>Source Currency</option>
                  <option value={"EUR"}>EUR</option>
                  <option value={"GBP"}>GBP</option>
                  <option value={"INR"}>INR</option>
                  <option value={"RMB"}>RMB</option>
                  <option value={"USD"}>USD</option>
                </select>
              </div>


              <div className="form-group">
            <label className="col-form-label w-100 text-left">
             Amount
            </label>
                <input
                  type="text"
                  name="amount"
                  className="form-control"
                  placeholder="Amount "
                  onChange={this.handleChange}
                  defaultValue={this.state.amount}
                />
              </div>

              <div className="form-group">
            <label className="col-form-label w-100 text-left">
            Expiry Date &nbsp;
            </label>
              <TextField
    id="datetime-local"
    type="datetime-local"
    name="expiry"
    
    onChange={this.handleChange}
  
  />
  </div>

              <div className="form-group">
            <label className="col-form-label w-100 text-left">
             Allow Counter Offers
            </label>
            <select
              className="form-control form-control-sm"
              name="allowCounterOffers"
              value = {this.state.allowCounterOffers}
              onChange={this.handleChange}
            >
              <option value="">Select</option>
              <option value={true}>Yes</option>
              <option value={false}>No</option>
            </select>
          </div>
              
          <div className="form-group">
            <label className="col-form-label w-100 text-left">
             Allow Split Exchanges
            </label>
            <select
              className="form-control form-control-sm"
              name="allowSplitExchanges"
              value = {this.state.allowSplitExchanges}
              onChange={this.handleChange}
            >
              <option value="">Select</option>
              <option value={true}>Yes</option>
              <option value={false}>No</option>
            </select>
          </div>


          <div className="form-group">
            <label className="col-form-label w-100 text-left">
             Use Prevailing Rate
            </label>
            <select
              className="form-control form-control-sm"
              name="usePrevailingRate"
              value = {this.state.usePrevailingRate}
              onChange={this.handleChange}
            >
              <option value="">Select</option>
              <option value={true}>Yes</option>
              <option value={false}>No</option>
            </select>
          </div>
              
          <div className="form-group">
            <label className="col-form-label w-100 text-left">
             Set Exchange Rate
            </label>
                <input
                  type="text"
                  name="exchangeRate"
                  className="form-control"
                  placeholder="exchangeRate "
                  onChange={this.handleChange}
                  defaultValue={this.state.exchangeRate}
                />
              </div>


              <div className="form-group">
                <Button  style={{backgroundColor:"blue"}} onClick={this.handleAdd}>
                  Update Listing!
                </Button>
              </div>
            </div>
           </div>
           </form>
           </div>
            
             
             
             
              
    );
  }
}

export default CreateOffer;
