import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { Redirect } from 'react-router';
import bcrypt from 'bcryptjs';
import axios from 'axios';
import queryString from 'query-string'

var jwt = require('jsonwebtoken');


class Verification extends Component {
    constructor(props) {
        super(props);
        this.state = {
            verfied:false  
        }
    }
    componentDidMount() {
        const values = queryString.parse(this.props.location.search)
        console.log("props",values.username);
        let username=values.username;
        let url = process.env.REACT_APP_BACKEND_URL+'/user/verifyMail'+'?username='+username
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                if (response.status==200) {
                    this.setState({
                        verified:true
                    })
                }
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    verified:false
                })
            });;
    }
 render() {
        let displayform = null;
        if (this.state.verified){
            displayform=(<div>
            <div>
            <h1>VERFICATION SUCCESSFUL</h1>
            </div>
            <div>
            <Link to="/signin"><button type="submit" class="btn btn-primary">Sign In </button></Link> 
            </div>

            </div>)
        }
        else{

            displayform=(<div>
                <div>
                <h1>VERFICATION FAILED TRY TO SIGNUP AGAIN</h1>
                <div>
            <Link to="/home"><button type="submit" class="btn btn-primary">Sign Up </button></Link> 
            </div>
                </div>
                </div>)

        }
         
        return (
            <div style={{marginTop:"300px"}}>
            {displayform}
            </div>
                
                
                   
        )
    }
}

export default Forgot;