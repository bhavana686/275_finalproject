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

var jwt = require('jsonwebtoken');
const nodemailer = require('nodemailer');

class SignUp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            placeholder: true,
            email: "",
            password: "",
            invalidEmail: false,
            invalidPassword: false,
            repeatPassword: "",
            passwordMatch: false,
            fname: "",
            lname: "",
            msg:"",
            signUpSuccessful: false,
            redirectToSignIn: false,
            passwordMatchError: false,
            signupFailedError: false,
            verified:false,
            mailSent:false
        }
        this.emailChangeHandler = this.emailChangeHandler.bind(this);
        this.passwordChangeHandler = this.passwordChangeHandler.bind(this);
        this.rePasswordChangeHandler = this.rePasswordChangeHandler.bind(this);
        this.validateDetails = this.validateDetails.bind(this);
        this.registerUser = this.registerUser.bind(this);
        this.handleDialogClose = this.handleDialogClose.bind(this);
        this.sendVerificationMail = this. sendVerificationMail.bind(this);
       
    
    }
     sendVerificationMail=async(email)=>{
        let targetMail=email;
        var token = jwt.sign({ mail: targetMail},"happy"); 
        let url = process.env.REACT_APP_BACKEND_URL+'/offers/email';
        let temp=process.env.REACT_APP_FRONTEND_URL+'/verifyMail?username='+targetMail;
        var data = {
            "sendto":targetMail,
            "subject":"direct exchnage email verification",
            "message": "Click the link to verify your email -"+temp
        }
        axios.post(url, data)
        .then(response => {
            if (response.status==200) {
                this.setState({
                    mailSent: true,
                    
                })
            }
            else
            {
                this.setState({
                    mailSent: false,
                })
            }
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                mailSent: false,
                
            })
        });;
    }
            
        

    registerUser = (event) => {
        event.preventDefault();
        let url = process.env.REACT_APP_BACKEND_URL+'/user';
        let encryptPassword = ""
        const salt = bcrypt.genSaltSync(1);
        encryptPassword = bcrypt.hashSync(this.state.password, salt);
        var data = {
            "username": this.state.email,
            "password": encryptPassword,
            "nickname": this.state.fname,
            "signupType":"general",
            "isVerified":false
        }
        axios.defaults.withCredentials = true;
        axios.post(url, data)
            .then(response => {
                if (response.data.username!=null && response.data.isVerified) {
                    this.setState({
                        signUpSuccessful: true,
                        verified:true,
                    })
                }
                else if (response.data.username!=null && !response.data.isVerified) {
                    this.sendVerificationMail(response.data.username);
                    this.setState({
                        signUpSuccessful: true,
                        verified:false
                    })
                }
                 else if (response.data.username==null && response.status ===200 ) {
                    this.setState({
                        signUpSuccessful: false,
                        signupFailedError: true,
                        verified:false,
                        msg:response.data,
                    })
                }
                else{
                }
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    signUpSuccessful: false,
                    signupFailedError: true,
                    verified:false,
                })
            });;
    }

    changeHandler = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        })
    }

    emailChangeHandler = (event) => {
        if (/.+@.+\.[A-Za-z]+$/.test(event.target.value)) {
            this.setState({
                invalidEmail: false,
                email: event.target.value
            })
        } else {
            this.setState({
                invalidEmail: true,
                email: event.target.value
            })
        }
    }

    passwordChangeHandler = (event) => {
        if (event.target.value.length > 5) {
            this.setState({
                password: event.target.value,
                invalidPassword: false
            })
            if (this.state.repeatPassword !== event.target.value && this.state.repeatPassword !== "") {
                this.setState({
                    passwordMatch: false
                })
            }
        } else {
            this.setState({
                password: event.target.value,
                invalidPassword: true
            })
        }
    }

    rePasswordChangeHandler = (event) => {
        if (this.state.password === event.target.value) {
            this.setState({
                repeatPassword: event.target.value,
                passwordMatch: true,
                passwordMatchError: false
            })
        } else {
            this.setState({
                repeatPassword: event.target.value,
                passwordMatch: false,
                passwordMatchError: true
            })
        }
    }

    validateDetails = (event) => {
        if (!this.state.invalidEmail && !this.state.invalidPassword && this.state.passwordMatch && this.state.fname !== "" && this.state.lname !== "") return false
        else return true
    }
    handleDialogClose = () => {
        this.setState({
            redirectToSign :true
        })
    }
    render() {
        let redirectToSign = null;
        if (this.state.redirectToSignIn) redirectToSign = <Redirect to="/signin" />
        return (
            <div>
                {redirectToSign}
                <Dialog
                    open={this.state.signUpSuccessful}
                    onClose={this.handleDialogClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">{"Registered Successfully .!"}</DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            Hey {this.state.name}, You've been signup succesfully. Please go ahead and verify your email
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleDialogClose} color="primary" autoFocus>
                            ok
                        </Button>
                    </DialogActions>
                </Dialog>
                
                   
                                <h3 class="pt-8 text-2xl text-center font-bold mb-6">Create an Account!</h3>
                                <form class="px-8 pt-6 pb-8 mb-4 bg-white rounded" onSubmit={this.registerUser}>
                                    <div class="mb-4 md:flex md:justify-between ">
                                        <div class="mb-4 md:mr-2 md:mb-0 xl:w-2/4 lg:w-6/12">
                                            <label class="block  tracking-wide text-gray-700 text-xl font-bold mb-2" for="fname">
                                                First Name
									        </label>
                                            <input
                                                class="w-full px-3 h-12 py-2 text-xl leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
                                                id="fname"
                                                name="fname"
                                                type="text"
                                                placeholder="First Name"
                                                onChange={this.changeHandler}
                                            />
                                        </div>
                                        <div class="md:ml-2 xl:w-2/4 lg:w-6/12">
                                            <label class="block  tracking-wide text-gray-700 text-xl font-bold mb-2" for="lname">
                                                Last Name
									        </label>
                                            <input
                                                class="w-full px-3 h-12 py-2 text-xl leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
                                                id="lname"
                                                name="lname"
                                                type="text"
                                                placeholder="Last Name"
                                                onChange={this.changeHandler}
                                            />
                                        </div>
                                    </div>
                                    <div class="mb-4">
                                        <label class="block  tracking-wide text-gray-700 text-xl font-bold mb-2" for="email">
                                            Email
								        </label>
                                        <input
                                            class="w-full px-3 py-2 mb-3 h-12 text-xl leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
                                            id="email"
                                            type="email"
                                            placeholder="Email"
                                            onChange={this.emailChangeHandler}
                                        />
                                        {this.state.invalidEmail && <p class="text-sm font-bold italic text-red-500">Please enter a valid Email.</p>}
                                    </div>
                                    <div class=" md:flex md:justify-between">
                                        <div class="mb-4 md:mr-2 md:mb-0 xl:w-2/4 lg:w-6/12">
                                            <label class="block  tracking-wide text-gray-700 text-xl font-bold mb-2" for="password">
                                                Password
									        </label>
                                            <input
                                                class="w-full px-3 py-2 mb-3 h-12 text-xl leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
                                                id="password"
                                                type="password"
                                                placeholder="******************"
                                                onChange={this.passwordChangeHandler}
                                            />
                                            {/* <p class="text-xs italic text-red-500">Please choose a password.</p> */}
                                        </div>
                                        <div class="md:ml-2 xl:w-2/4 lg:w-6/12">
                                            <label class="block  tracking-wide text-gray-700 text-xl font-bold mb-2" for="repeatPassword">
                                                Confirm Password
									        </label>
                                            <input
                                                class="w-full px-3 py-2 mb-3 h-12 text-xl leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
                                                id="repeatPassword"
                                                type="password"
                                                placeholder="******************"
                                                onChange={this.rePasswordChangeHandler}
                                            />
                                        </div>
                                    </div>
                                    {(this.state.invalidPassword || this.state.passwordMatchError) && <p class="text-sm font-bold italic text-red-500">Password's doesn't match or Invalid.</p>}
                                    <div class="form-group" style={{ "alignItems": "center" }}>
                                        {this.state.signupFailedError ? <span style={{ color: "red", "font-style": "oblique", "font-weight": "bold", "textAlign": "center" }}>SignUp Failed. Please try again.{this.state.msg}</span> : ''}
                                    </div>
                                    <div class="mt-3 mb-6 text-center">
                                        <button
                                            class="w-full px-4 py-2 font-bold text-white bg-blue-500 rounded-full hover:bg-blue-700 focus:outline-none focus:shadow-outline"
                                            type="submit"
                                            disabled={this.validateDetails()}
                                        >
                                            Register Account
								        </button>
                                    </div>
                                    <hr class="mb-6 border-t" />
                                    
                                    <div class="text-center">
                                        <a
                                            class="inline-block text-xsmd text-blue-500 align-baseline hover:text-blue-800"
                                            href="/signin"
                                        >
                                            Already have an account? Login!
								        </a>
                                    </div>
                                </form>
                            </div>
                    
                      
        )
    }
}

export default SignUp;