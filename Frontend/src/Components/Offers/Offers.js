import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import {Button,ButtonGroup} from 'react-bootstrap';
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




class Offers extends Component {
  constructor(props) {
    super(props);
    this.state = {
        sourceCurrency:"",
        targetCurrency:"",
        exchangeRate:"",
        offers:[],
        edit: false,
        id: sessionStorage.getItem("id")
    }
    this.handleUpdate = this.handleUpdate.bind(this);
    this.ChangeHandler = this.ChangeHandler.bind(this);
    
}

handleUpdate(id){
    console.log("in here")
    this.setState({
        redirect:`/editoffer/${id}`
    })
}

    
componentDidMount() {
    let url = process.env.REACT_APP_BACKEND_URL+"/offer/"+this.state.id;
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
        let details=null;
        var displayform=null,displayform2=null;
        displayform2 = this.state.offers?this.state.offers.map((msg) => {
           }):"";

        
        displayform = this.state.offers.map((msg) => {
               return (
                <div>
                <div class="form-group row" style={{marginLeft:"20px"}}>
                          
                            <div class="col-lg-4">
                     <Card style={{ height: "180px",width:"1000px" ,textAlign:"left" }}>
                      <CardContent> 
                      <div class="row">
                       <div class="col-lg-1"> 
                      <LocationOnIcon style={{ color: brown[400], fontSize: 60 }} /> 
                      </div>
                      <div class="col-lg-3"> 
                      <div style={{marginTop:"10px"}}>Source-<b>{msg.sourceCountry}</b><br></br>Destination-<b>{msg.destinationCountry}</b></div>
                      </div>
                      <div class="col-lg-1"> 
                      <MonetizationOnIcon style={{ color: brown[400], fontSize: 60 }} /> 
                      </div>
                      <div class="col-lg-3"> 
                      <div style={{marginTop:"10px"}}>Source-<b>{msg.sourceCurrency}</b><br></br>Destination-<b>{msg.destinationCurrency}</b></div>
                      </div>
                      <div class="col-lg-1"> 
                      <PermIdentityIcon  style={{ color: brown[400], fontSize: 60 }} /> 
                      </div>
                      <div class="col-lg-3"> 
                      <div style={{marginTop:"20px"}}><b>{msg.postedBy ? msg.postedBy.nickname:""}</b></div>
                      </div>
                      </div> 
                      
                     <div class="row" style={{marginTop:"20px"}} >

                        <div class="class" style={{marginLeft:"20PX"}}> Counter Offer Status  {msg.allowCounterOffers?      <FlagIcon style={{ color: green[400], fontSize: 20 }} />  :      <FlagIcon style={{ color: red[400], fontSize: 60 }} /> }&nbsp;&nbsp;
                        
                           Split Offer Status  {msg.allowSplitExchanges?      <FlagIcon style={{ color: green[400], fontSize: 20 }} />  :      <FlagIcon style={{ color: red[400], fontSize: 60 }} /> }
                        
                        </div>
                        </div> 
                        <div class="row" >

<div class="class" style={{marginLeft:"20PX"}}> <b>{msg.amount}</b> Amount availabe  for Exchange rate <b>{msg.exchangeRate}</b></div>
</div>

                
                      


                        <div class="row">
                        <div class="redclass" style={{color:green ,marginLeft:"20PX"}}> status is {msg.status}</div>
                        </div>
                        
                        
                        
                        
                     
                       

                       </CardContent>
                     </Card>

                
             </div>
             </div></div>
               
            )})


            
        

    return (
        <div style={{marginTop:"50px"}}>
         <div style={{marginLeft:"700px"}}>
         {redirectVar}
         <Link to="/createoffer"><button type="submit" class="btn btn-primary">Create Offer</button></Link>
         
         </div>
            {displayform}
        </div>
        );
     }
}





export default Offers;