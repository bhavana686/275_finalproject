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

class ExchangeCurrency extends Component {
  constructor(props) {
    super(props);
    this.state = {
        sourceCurrency:"",
        targetCurrency:"",
        exchangeRate:"",
        useracc:[],
        edit: false,
    }
    this.ChangeHandler = this.ChangeHandler.bind(this);
    
}
    
componentDidMount() {
    let url = process.env.REACT_APP_BACKEND_URL+'/exchangeRate/getAll';
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
        let editform = null;
        let details=null;
        var displayform=null;
        displayform = (

            this.state.useracc.map(item => {
                return (
                    <div>
                    <div class="form-group row" >
                                <div class="col-lg-3">        </div>
                                <div class="col-lg-4">
                         <Card style={{ height: "100px",width:"500px" ,textAlign:"left" }}>
                          <CardContent> 
                          <div class="row">
                            <div class="col-lg-6"> Source Currency</div>
                            <div class="col-lg-6">  {item.sourceCurrency}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">  Target Currency</div>
                            <div class="col-lg-6">  {item.targetCurrency}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6"> Exchange Rate</div>
                            <div class="col-lg-6">  {item.exchangeRate}</div>
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





export default ExchangeCurrency;