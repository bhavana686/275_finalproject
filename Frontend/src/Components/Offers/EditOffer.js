import React, { Component, Fragment } from "react";
import axios from "axios";
import { Redirect } from "react-router";
import { Button } from "react-bootstrap";
import Icon from "@material-ui/core/Icon";
import '../Offers/editoffer.css';
import Moment from 'moment';
import TextField from '@material-ui/core/TextField';


class EditOffer extends Component {
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
      offerid: this.props.match.params.id,
      success:""
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleAdd = this.handleAdd.bind(this);
    this.validateDetails = this.validateDetails.bind(this);

  }

  async componentDidMount() {
    axios
      .get(
        process.env.REACT_APP_BACKEND_URL+'/offer/currentoffer/'+this.state.offerid
      )
      .then((response) => {
        console.log("Pro are::", response.data);
        this.setState({
            allowCounterOffers: response.data[0].allowCounterOffers,
            allowSplitExchanges: response.data[0].allowSplitExchanges,
            amount: response.data[0].amount,
            counter: response.data[0].counter,
            destinationCountry: response.data[0].destinationCountry,
            destinationCurrency: response.data[0].destinationCurrency,
            editable: response.data[0].editable,
            exchangeRate: response.data[0].exchangeRate,
            expiry: Moment(response.data[0].expiry).format("YYYY-MM-DDTHH:mm:ss"),
            fullyFulfilled: response.data[0].fullyFulfilled,
            id: response.data[0].id,
            sourceCountry: response.data[0].sourceCountry,
            sourceCurrency: response.data[0].sourceCurrency,
            status: response.data[0].status,
            transactedAmount: response.data[0].transactedAmount,
            usePrevailingRate: response.data[0].usePrevailingRate,
         
        });
        console.log(response.data[0].expiry)
        console.log(typeof(Moment(response.data[0].expiry).format("YYYY-MM-DDTHH:mm")))
      });
  }

  validateDetails = (event) => {

    if (this.state.expiry!="" &&    !isNaN(parseFloat(this.state.amount)) &&
    ((this.state.sourceCurrency=="USD" && this.state.sourceCountry=="US") || (this.state.sourceCurrency=="INR" && this.state.sourceCountry=="India") 
    || (this.state.sourceCurrency=="EUR" && this.state.sourceCountry=="Europe") || (this.state.sourceCurrency=="GBP" && this.state.sourceCountry=="UK") 
    || (this.state.sourceCurrency=="RMB" && this.state.sourceCountry=="China") ) &&
    ((this.state.destinationCurrency=="USD" && this.state.destinationCountry=="US") || (this.state.destinationCurrency=="INR" && this.state.destinationCountry=="India") 
    || (this.state.destinationCurrency=="EUR" && this.state.destinationCountry=="Europe") || (this.state.destinationCurrency=="GBP" && this.state.destinationCountry=="UK") 
    || (this.state.destinationCurrency=="RMB" && this.state.destinationCountry=="China")) &&
    (this.state.destinationCountry != this.state.sourceCountry) && (this.state.destinationCurrency != this.state.sourceCurrency)
    ) return false
    else return true 
   
    
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
        expiry: this.state.expiry+":00",
        fullyFulfilled: this.state.fullyFulfilled,
        id: this.state.id,
        sourceCountry: this.state.sourceCountry,
        sourceCurrency: this.state.sourceCurrency,
        status: this.state.status,
        transactedAmount: this.state.transactedAmount,
     username : sessionStorage.getItem("username"),
        usePrevailingRate: this.state.usePrevailingRate,
   
    };

    let url = process.env.REACT_APP_BACKEND_URL+"/offer/"+this.state.offerid;

    //set the with credentials to true
    axios.defaults.withCredentials = true;
    //make a post request with the user data
    console.log("req.body", data);
    axios
      .put(url, data)
      .then((response) => {
        console.log(response);
        if (response.data === "error") {
          console.log("error");
          this.setState({
            showRegistrationError: true,
          });
        } else if (response.data === "updated") {
          this.setState({
            success : "Successfully Updated the Offer"
          });
        }
        else if(response.data === "low accounts"){
          this.setState({
            showmsg: "Please Create the bank Accounts in the given Countries"
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
    let redirectVar = null, msgshow=null;
    if (this.state.redirect) {
      redirectVar = <Redirect push to={this.state.redirect} />;
    }
    if(this.state.success){
      msgshow = <h2 style={{color:"red"}}>Succesfully Updated the Offer</h2>
    }
console.log(this.state.expiry)
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
          {msgshow}
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
    defaultValue={this.state.expiry}
    value={this.state.expiry}
    onChange={this.handleChange}/>
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

          <div>
              {this.state.usePrevailingRate == "false" ? (
                <div>
                   <label className="col-form-label w-100 text-left">
                  Exchange Rate
            </label>
                  <div className="form-group">
                    <input
                      type="number"
                      name="exchangeRate"
                      className="form-control"
                      placeholder="Exchange Rate"
                      onChange={this.handleChange}
                    />{" "}
                  </div>
                
                </div>
              ) : (
                ""
              )}
              </div>
              
     
              <div className="form-group">
                <Button disabled={this.validateDetails()} style={{backgroundColor:"blue"}} onClick={this.handleAdd}>
                  Update Offer!
                </Button>
              </div>
            </div>
           </div>
           </form>
           </div>
            
             
             
             
              
    );
  }
}

export default EditOffer;
