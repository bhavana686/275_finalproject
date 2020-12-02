import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { CenterFocusStrong, SystemUpdate } from '@material-ui/icons';
import landingpage from "../Landingpage";
import FormControl from '@material-ui/core/FormControl';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import FormHelperText from '@material-ui/core/FormHelperText';
const names = [
    'sending',
    'receiving',
    'both',
    
  ];

  const country = [
    'Europe', 'UK', 'India', 'China', 'US',
  ];
  const currency = [
    'EUR', 'GBP', 'INR', 'RMB', 'USD'
    
  ];

class AddBankAccount extends Component {
  constructor(props) {
    super(props);
    this.state = {
     bankName:"",
     accountNumber:"",
   ownerName:"",
     ownerAddress:"",
     accountType:"",
     country:"",
     primaryCurrency:"",
     useracc:"",
     flag:false
    }
    this.ChangeHandler = this.ChangeHandler .bind(this);
    this.registerUser = this.registerUser.bind(this);
    this.HandleChange = this.HandleChange.bind(this);
    this.currencyHandleChange = this.currencyHandleChange.bind(this);
    this.countryHandleChange = this.countryHandleChange.bind(this);
}


ChangeHandler = (event) => {
    console.log(event.target.name);
    console.log(event.target.value)
    this.setState({
        [event.target.name]: event.target.value
    })
}

HandleChange = (event) => {
    console.log(event.target.value)
    this.setState({
        accountType : event.target.value
    })
}
currencyHandleChange = (event) => {
    this.setState({
        primaryCurrency : event.target.value
    })
}
countryHandleChange = (event) => {
    console.log(event.target.value)
    this.setState({
        country : event.target.value
    })
}

registerUser = (event) => {
    event.preventDefault();
    let id=sessionStorage.getItem("id");
    let url = process.env.REACT_APP_BACKEND_URL+'/bank/'+id+'?primaryCurrency='+this.state.primaryCurrency+'&country='+this.state.country;
    console.log(url);
    var data = {
        bankName:this.state.bankName,
        accountNumber:this.state.accountNumber,
        ownerName:this.state.ownerName,
        ownerAddress:this.state.ownerAddress,
        accountType:this.state.accountType,
    }
    console.log("call to back");
    axios.defaults.withCredentials = true;
    axios.post(url, data)
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
        let homev = null;
        if (this.state.flag) {
          homev = <Redirect to="/bankAccount" />
        }
        return (
          
          <div>
            {homev}
        <br />
        <form onSubmit={this.registerUser}>
            <div class="container" >
                <div class="row">
                    <div class="col-lg-10 col-xl-9 mx-auto">
                        <div class="card card-signin flex-row my-5">

                            <div class="card-body" style={{marginBottom:"30px",marginRight:"300px"}}>
                                <h5> <b>ADD BANK ACCOUNT</b></h5>
                            </div>

                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="bankName">Bank name:</label>
                                <div class="col-sm-5">
                                    <input type="text" name="bankName" id="bankName" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="accountNumber">Account Number:</label>
                                <div class="col-sm-5">
                                    <input type="text" name="accountNumber" id="accountNumber" onChange={this.ChangeHandler} class="form-control" required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                            <label class="control-label col-sm-2" for=" ownerName">Owner Name:</label>
                            <div class="col-sm-5">
                                    <input type="text" name="ownerName" id="ownerName" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="ownerAddress">Owner Address:</label>
                                <div class="col-sm-5">
                                <input type="text" name="ownerAddress" id="ownerAddress" onChange={this.ChangeHandler} class="form-control" required />
                        </div>
                        </div><br /><br />
                        
                    <div class="form-label-group">
                        <label class="control-label col-sm-2" for="accountType">Account Type</label>    
                            <div class="col-sm-1" >
                             <Select
        
                                     id="accountType"
                                     value={this.state.accountType}
                                     onChange={this.HandleChange}>
        
                                        <MenuItem value={names[0]} >sending</MenuItem>
                                         <MenuItem value={names[1]}>receiving</MenuItem>
                                         <MenuItem value={names[2]}>both</MenuItem>
                            </Select>
    
                        </div>
                    </div>
                    <br /><br />
                    <div class="form-label-group">
                        <label class="control-label col-sm-2" for="country">Country</label>    
                            <div class="col-sm-1">
                                <Select
                                    id="country"
                                     value={this.state.country}
                                     onChange={this.countryHandleChange}>
                                      <MenuItem value={country[0]} >Europe</MenuItem>
                                       <MenuItem value={country[1]}>UK</MenuItem>
                                      <MenuItem value={country[2]}>India</MenuItem>
                                      <MenuItem value={country[3]}>China</MenuItem>
                                      <MenuItem value={country[4]}>US</MenuItem>
                                </Select>
                         </div>
                    </div>
                    <br /><br />
                    <div class="form-label-group">
                        <label class="control-label col-sm-2" for="primaryCurrency">Primary Currency</label>    
                            <div class="col-sm-1">
                                  <Select
        
                                       id="primaryCurrency"
                                       value={this.state.primaryCurrency}
                                        onChange={this.currencyHandleChange}
                                         >
                                        <MenuItem value={currency [0]} >EUR</MenuItem>
                                          <MenuItem value={currency [1]}>GBP</MenuItem>
                                           <MenuItem value={currency [2]}>INR</MenuItem>
                                          <MenuItem value={currency [3]}>RMB</MenuItem>
                                          <MenuItem value={currency [4]}>USD</MenuItem>

                                    </Select>
                             </div>
                    </div>
                    <br /><br />
                    
            <div class="form-group">
            <div class="col-sm-10">
            <button onClick={this.registerUser} class="btn btn-primary" type="submit">Add</button>&nbsp;

              
                <Link to="/bankAccount"><button type="submit" class="btn btn-primary">Cancel</button></Link>
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


export default AddBankAccount;