import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { CenterFocusStrong } from '@material-ui/icons';
import landingpage from "../Landingpage";
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import EditIcon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import {Edit} from '@material-ui/icons';

class BankAccount extends Component {
  constructor(props) {
    super(props);
    this.state = {
        useracc:[],
      
    }
    this.ChangeHandler = this.ChangeHandler.bind(this);
}

componentDidMount() {
    let id=sessionStorage.getItem("id");
    let url = process.env.REACT_APP_BACKEND_URL+'/bank/'+id;
    console.log(url);
    axios.defaults.withCredentials = true;
    axios.get(url)
        .then(response => {
                this.setState({
                    useracc:response.data
                })
                console.log(this.state.useracc)
            
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                useracc:[]
            })
        });;
}


ChangeHandler = (event) => {
    this.setState({
        [event.target.name]: event.target.value
    })
}
    render() {
        var displayform=null;
        displayform = (

            this.state.useracc.map(item => {
                return (
                    <div>
                    <div class="form-group row" >
                                <div class="col-lg-3">        </div>
                                <div class="col-lg-4">
                         <Card style={{ height: "180px",width:"500px" ,textAlign:"left" }}>
                          <CardContent> 
                          <div class="row">
                            <div class="col-lg-6">  Bank Name</div>
                            <div class="col-lg-6">  {item.bankName}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Account Number</div>
                            <div class="col-lg-6">  {item.accountNumber}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Owner Name</div>
                            <div class="col-lg-6">  {item.ownerName}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Owner Address</div>
                            <div class="col-lg-6">  {item.ownerAddress}</div>
                            </div>

                            <div class="row">
                            <div class="col-lg-6"> Account Type:</div>
                            <div class="col-lg-6">  {item.accountType}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Country</div>
                            <div class="col-lg-6">  {item.country}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Primary Currency:</div>
                            <div class="col-lg-6">  {item.primaryCurrency}</div>
                            </div>
                           </CardContent>
                         </Card>

                    
                 </div>
                 </div></div>
                )
            }))
        

    return (
        <div style={{marginTop:"50px"}}>
            {displayform}
        </div>
        );
     }
}





export default BankAccount;