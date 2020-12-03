import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import EditIcon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import {Edit} from '@material-ui/icons';
import { Button } from "react-bootstrap";
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
import { Badge, Space, Row, message, Modal, Input } from 'antd';


class BankAccount extends Component {
  constructor(props) {
    super(props);
    this.state = {
        useracc:[],
        redirect:""
      
    }
    this.ChangeHandler = this.ChangeHandler.bind(this);
    this.handleaddnew = this.handleaddnew.bind(this);
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

handleaddnew()
{
    console.log("in here")
    this.setState({
        redirect:'/addBankAccount/'
    })
}

ChangeHandler = (event) => {
    this.setState({
        [event.target.name]: event.target.value
    })
}
    render() {
        var displayform=null,redirectvar=null;
        if(this.state.redirect)
        {

            
                redirectvar = <Redirect push to={this.state.redirect} />;
              
        }
        displayform = (

            this.state.useracc.map(msg => {

              
                    return (
                        <div>
                            &nbsp;
                          

                               
                            
                                <Card style={{ textAlign: "left", margin :"30px" }} >
                                    
                                    <CardContent>
                                        <div class="row">
                                            <div class="col-lg-1">
                                                <MonetizationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                            </div>
                                            <div class="col-lg-3">
                                                <div style={{ marginTop: "10px" }}>Account Number-<b>{msg.accountNumber}</b><br></br>Primary Currency-<b>{msg.primaryCurrency}</b><br></br>Location-<b>{msg.country}</b></div>
                                            </div>
                                            <div class="col-lg-1">
                                                <MonetizationOnIcon style={{ color: brown[400], fontSize: 60 }} />
                                            </div>
                                            <div class="col-lg-3">
                                                <div style={{ margin: "10px" }}>Bank Name-<b>{msg.bankName}</b><br></br>Account Type-<b>{msg.accountType}</b></div>
                                            </div>
                                            <div class="col-lg-1">
                                                <PermIdentityIcon style={{ color: brown[400], fontSize: 60 }} />
                                            </div>
                                            <div class="col-lg-3">
                                                <div style={{ marginTop: "20px" }}><b>{msg.ownerName} </b></div>
                                            </div>
                                           
                                        </div>
                                        
                                    
                                    </CardContent>
                                </Card>
                        </div>
                    )}))
        
    return (
        <div style={{marginTop:"50px"}}>
                     {redirectvar}

                     <Row className="mt-4 items-center" align="center">
                                <Link to={"/addBankAccount/"}><Button type="primary">Add Bank Account</Button></Link>&nbsp;
                                

                                
                                </Row>
            {displayform}
        </div>
        );
     }
}





export default BankAccount;