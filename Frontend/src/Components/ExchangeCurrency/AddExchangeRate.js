import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { CenterFocusStrong } from '@material-ui/icons';
import landingpage from "../Landingpage";
import exchangeCurrency from "./ExchangeCurrency"
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';

const currency = [
    'EUR', 'GBP', 'INR', 'RMB', 'USD'
    
  ];

class AddExchangeRate extends Component {
  constructor(props) {
    super(props);
    this.state = {
        sourceCurrency:"",
        targetCurrency:"",
        exchangeRate:"",
        useracc:"",
        flag:false,
        invalidrate:false
    }
    this.ChangeHandler = this.ChangeHandler.bind(this);
    this.registerExchange = this.registerExchange.bind(this);
    this.validateDetails = this.validateDetails.bind(this);
    this.sourcecurrencyHandleChange = this.sourcecurrencyHandleChange.bind(this);
    this.targetcurrencyHandleChange = this.targetcurrencyHandleChange.bind(this);
}

ChangeHandler = (event) => {
    if(!(/^[a-zA-Z]+$/.test(event.target.value))){
        this.setState({
            invalidrate: true,
            [event.target.name]: event.target.value
        })
      }
      else
      {
        this.setState({
            invalidrate: false,
            [event.target.name]: event.target.value
        })

      } 
   
}
sourcecurrencyHandleChange = (event) => {
    this.setState({
        sourceCurrency : event.target.value
    })
}
targetcurrencyHandleChange = (event) => {
    this.setState({
        targetCurrency : event.target.value
    })
}
validateDetails = (event) => {
    if ((this.state.sourceCurrency==this.state.targetCurrency)  && ((/^[a-zA-Z]+$/.test(this.state.exchangeRate)))) return true
    else return false
}

registerExchange = (event) => {
    event.preventDefault();
    let url = process.env.REACT_APP_BACKEND_URL+'/exchangeRate'+'?sourceCurrency='+this.state.sourceCurrency+'&targetCurrency='+this.state.targetCurrency+'&exchangeRate='+this.state.exchangeRate;
    axios.defaults.withCredentials = true;
    axios.post(url)
        .then(response => {
            console.log(response)
            if (response.status ===200) {
                this.setState({
                    useracc:response.data,
                    flag:true
                })
            }
            else{ 
                this.setState({
                useracc:"",
                flag:false
            })

            }
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                useracc:"",
                flag:false
            })
        });;
}

    render() {
        let redirectToDisplay = null;
        if (this.state.flag) redirectToDisplay = <Redirect to="/exchangeCurrency" />
    return (
        <div >
        {redirectToDisplay}
        <br />
        <form onSubmit={this.registerExchange}>
            <div class="container" >
                <div class="row">
                    <div class="col-lg-10 col-xl-9 mx-auto">
                        <div class="card card-signin flex-row my-5">

                            <div class="card-body" style={{marginBottom:"30px",marginRight:"300px"}}>
                                <h5> <b>ADD CURRENCY EXCHANGE RATE</b></h5>
                            </div>

                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="sourceCurrency">Source Currency:</label>
                                <div class="col-sm-5">
                                  <div class="form-label-group">
                                     <div class="col-sm-1">
                                          <Select
        
                                       id="sourceCurrency"
                                       value={this.state.sourceCurrency}
                                        onChange={this.sourcecurrencyHandleChange}
                                         >
                                        <MenuItem value={currency [0]} >EUR</MenuItem>
                                          <MenuItem value={currency [1]}>GBP</MenuItem>
                                           <MenuItem value={currency [2]}>INR</MenuItem>
                                          <MenuItem value={currency [3]}>RMB</MenuItem>
                                          <MenuItem value={currency [4]}>USD</MenuItem>

                                    </Select>
                                  </div>
                               </div>

                            </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                            <label class="control-label col-sm-2" for="targetCurrency">Target Currency:</label>
                                <div class="col-sm-5">
                                  <div class="form-label-group">
                                     <div class="col-sm-1">
                                          <Select
        
                                       id="targetCurrency"
                                       value={this.state.targetCurrency}
                                        onChange={this.targetcurrencyHandleChange}
                                         >
                                        <MenuItem value={currency [0]} >EUR</MenuItem>
                                          <MenuItem value={currency [1]}>GBP</MenuItem>
                                           <MenuItem value={currency [2]}>INR</MenuItem>
                                          <MenuItem value={currency [3]}>RMB</MenuItem>
                                          <MenuItem value={currency [4]}>USD</MenuItem>

                                    </Select>
                                  </div>
                               </div>

                            </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                            <label class="control-label col-sm-2" for="exchangeRate">Exchange Rate:</label>
                            <div class="col-sm-5">
                                    <input type="text" name="exchangeRate" id="exchangeRate" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                            
                   
            <br /><br />
            <div class="form-group">
            <div class="col-sm-10">
            <button  disabled={this.validateDetails()} onClick={this.submitEvent} class="btn btn-primary" type="submit">Add</button>&nbsp;

              
                <Link to="/exchangeCurrency"><button type="submit" class="btn btn-primary">Cancel</button></Link>
                </div>

            </div>
            
           
</div>
</div>
</div>
</div>

</form>
</div>



                  
    )
}
}

export default AddExchangeRate;