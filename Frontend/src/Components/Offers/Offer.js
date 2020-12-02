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

const currency = [
    'EUR', 'GBP', 'INR', 'RMB', 'USD'
    
  ];



class Offer extends Component {
  constructor(props) {
    super(props);
    this.state = {
        sourceCurrency:"",
        destinationCurrency:"",
        sourceAmount:"",
        destinationAmount:"",
        offers:[],
        edit: false,
    }
 
    this.ChangeHandler = this.ChangeHandler.bind(this);
    this.filterOffer= this.filterOffer.bind(this);
    this.dcHandleChange= this.dcHandleChange.bind(this);
    this.currencyHandleChange= this.currencyHandleChange.bind(this);
    this.validateDetails= this.validateDetails.bind(this);
    
}
    
componentDidMount() {
    let url = process.env.REACT_APP_BACKEND_URL+'/offers'
    console.log(url);
    axios.defaults.withCredentials = true;
    axios.get(url)
        .then(response => {
                this.setState({
                    offers:response.data
                })
                console.log(this.state.offers)
            
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                offers:[]
            })
        });;
}
filterOffer = (event) => {
    event.preventDefault();
  let url = process.env.REACT_APP_BACKEND_URL +'/offers/filter?destinationAmount='+this.state.destinationAmount+'&sourceAmount='+this.state.sourceAmount+'&sourceCurrency='+this.state.sourceCurrency+'&destinationCurrency='+this.state.destinationCurrency;
    axios.defaults.withCredentials = true;
    console.log(url);
    axios.get(url)
        .then(response => {
            console.log(response)
            if (response.status ===200) {
                this.setState({
                    offers:response.data,
                    flag:true
                })
            }
            else{ 
                this.setState({
                    offers:"",
                flag:false
            })

            }
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                offers:"",
                flag:false
            })
        });;
}
dcHandleChange = (event) => {
    this.setState({
          destinationCurrency: event.target.value
    })
}
currencyHandleChange= (event) => {
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
    if (this.state.sourceCurrency !== "" && this.state.destinationCurrency  !== "" && this.state.sourceAmount !== "" && this.state.destinationAmount !== "" ) return false
    else return true
}




render() {
        var displayform=null;
        displayform = this.state.offers?this.state.offers.map((msg) => {
                return (
                    <div>
                    <div class="form-group row" >
                                <div class="col-lg-3">        </div>
                                <div class="col-lg-4">
                         <Card style={{ height: "250px",width:"500px" ,textAlign:"left" }}>
                          <CardContent> 
                          <div class="row">
                            <div class="col-lg-6"> Source Country</div>
                            <div class="col-lg-6"> {msg.sourceCountry}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Source Currency</div>
                            <div class="col-lg-6">  {msg.sourceCurrency}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">Destination Country</div>
                            <div class="col-lg-6">  {msg.destinationCountry}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">Destination Currency</div>
                            <div class="col-lg-6">  {msg.destinationCurrency}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">Status</div>
                            <div class="col-lg-6">  {msg.status}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">Amount</div>
                            <div class="col-lg-6">  {msg.amount}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Exchange Rate</div>
                            <div class="col-lg-6">  {msg.exchangeRate}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">Posted By </div>
                            <div class="col-lg-6">  {msg.postedBy ? msg.postedBy.nickname:""}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">Counter Offer Status </div>
                            <div class="col-lg-6">  {msg.allowCounterOffers? "Allowed" : "Not Allowed"}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">Split Offer Status  </div>
                            <div class="col-lg-6">  {msg.allowSplitExchanges?  "Allowed" : "Not Allowed"}</div>
                            </div>
    
                           </CardContent>
                         </Card>

                    
                 </div>
                 </div></div>
                   
                )}):"";

        let filterlist=(<div>
            <div><br />
                       <div className="container" >
                           <Grid container >
                               <div style={{ alignContent: "center", width: "100%", marginRight: "20px", paddingBottom: "10px" }}>       
                                   <Card style={{ height: "100%", marginBottom: "1px" }}>
                                   <h4 style={{ paddingLeft: "20px",marginTop:"10px",marginBottom: "4px" }}>SEARCH FILTER</h4>
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
                                          <MenuItem value={currency [0]} >EUR</MenuItem>
                                          <MenuItem value={currency [1]}>GBP</MenuItem>
                                           <MenuItem value={currency [2]}>INR</MenuItem>
                                          <MenuItem value={currency [3]}>RMB</MenuItem>
                                          <MenuItem value={currency [4]}>USD</MenuItem>

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
                                        <MenuItem value={currency [0]} >EUR</MenuItem>
                                          <MenuItem value={currency [1]}>GBP</MenuItem>
                                           <MenuItem value={currency [2]}>INR</MenuItem>
                                          <MenuItem value={currency [3]}>RMB</MenuItem>
                                          <MenuItem value={currency [4]}>USD</MenuItem>

                                      </Select>
                                        </div>
                                              </div>
                                               <div class="form-group row">
                                               <div class="col-sm-2"></div>
                                            <div class="col-sm-8">
                                            <input type="text" class="form-control" id="nameFilter" aria-describedby="search"  placeholder="Source Amount" name="sourceAmount" onChange={this.ChangeHandler} style={{ width: "100%", marginTop: "5px" }} />
                                             </div>
                                               </div>
                                               <div class="form-group row">
                                               <div class="col-sm-2"></div>
                                            <div class="col-sm-8">
                                            <input type="text" class="form-control" id="nameFilter" aria-describedby="search"  placeholder="Destination Amount" name="destinationAmount" onChange={this.ChangeHandler} style={{ width: "100%", marginTop: "5px" }} />
                                             </div>
                                               </div>
                                               <button onClick={this.submitEvent} class="btn btn-primary" type="submit"   disabled={this.validateDetails()}>GO</button>&nbsp;
                                            </form>
                                           </CardContent>
                                      </Card>
                                      </div> </Grid>
                                </div>
               
                          </div>

        </div>)


                 
        

    return (
        <div style={{marginTop:"50px"}}>
         <div class='row' >
                    <div class='col-md-3' >
                        <div>{filterlist}</div>
                    </div>
                    <div class='col-md-7' style={{marginTop:"2%"}}>
                        <div class= "grid-container">
                        {displayform}
                        </div>
                        
                    </div>
                </div>
        
           
        </div>
        );
     }
}





export default Offer;