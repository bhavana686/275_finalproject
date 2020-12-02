import React, {
	Component
} from 'react';
import Avatar from '@material-ui/core/Avatar';
import firebase from "firebase";
import GoogleLogin from 'react-google-login';
import GoogleLoginBtn from 'react-google-login';
import axios from 'axios';
import StyledFirebaseAuth from "react-firebaseui/StyledFirebaseAuth";
import FacebookLogin from 'react-facebook-login';
import FacebookLoginBtn from 'react-facebook-login';
import HouseIcon from '@material-ui/icons/House';
import SignUp from './Authentication/SignUp';
import Landingpage from './Landingpage';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import {
	Redirect
} from 'react-router';
import bcrypt from 'bcryptjs';


class Home extends Component {
	constructor(props) {
		super(props);
		this.state = {
			signUpSuccessful: false,
			signupFailedError: false,
			verified: false,
			msg: "",
			mailSent: false,
            redirectToSign: false,
            fbflag:false
		}
		this.responseGoogle = this.responseGoogle.bind(this);
		this.sendVerificationMail = this.sendVerificationMail.bind(this);
		this.responseFacebook = this.responseFacebook.bind(this);
        this.handleDialogClose = this.handleDialogClose.bind(this);
        this.componentClicked = this.componentClicked.bind(this);
	}
	sendVerificationMail = async(email) => {
		let targetMail = email;
		let url = process.env.REACT_APP_BACKEND_URL + '/offers/email';
		let temp = process.env.REACT_APP_FRONTEND_URL + '/verifyMail?username=' + targetMail;
		var data = {
			"sendto": targetMail,
			"subject": "direct exchnage email verification",
			"message": "Click the link to verify your email -" + temp
		}
		axios.post(url, data)
			.then(response => {
				if (response.status == 200) {
					this.setState({
						mailSent: true,

					})
				} else {
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

	responseGoogle = (response) => {
		let url = process.env.REACT_APP_BACKEND_URL + '/user';
		var data = {
			"username": response.profileObj.email,
			"password": response.profileObj.googleId,
			"nickname": response.profileObj.name,
			"signupType": "google",
			"isVerified": false
		}
		axios.defaults.withCredentials = true;
		axios.post(url, data)
			.then(response => {
				console.log(response);
				if (response.data.username != null) {
					this.sendVerificationMail(response.data.username);
					this.setState({
						signUpSuccessful: true,
					})
				} else if (response.data.username == null && response.status === 200) {
					this.setState({
						signUpSuccessful: false,
						signupFailedError: true,
						msg: response.data,
					})
				} else {}
			})
			.catch((error) => {
				console.log(error);
				this.setState({
					signUpSuccessful: false,
					signupFailedError: true

				})
			});;
	}


	responseFacebook = (response) => {
		console.log(response);
		let url = process.env.REACT_APP_BACKEND_URL + '/user';
		var data = {
			"username": response.email,
			"password": response.id,
			"nickname": response.name,
			"signupType": "facebook",
			"isVerified": false

		}
		axios.defaults.withCredentials = true;
		axios.post(url, data)
			.then(response => {
				console.log(response);
				if (response.data.username != null) {
					this.sendVerificationMail(response.data.username);
					this.setState({
						signUpSuccessful: true,
					})
				} else if (response.data.username == null && response.status === 200) {
					this.setState({
						signUpSuccessful: false,
						signupFailedError: true,
						msg: response.data,
					})
				} else {}
			})
			.catch((error) => {
				console.log(error);
				this.setState({
					signUpSuccessful: false,
					signupFailedError: true

				})
			});;
	}
	
	handleDialogClose = () => {
			this.setState({
				redirectToSignIn: true
			})
    }
    componentClicked=()=>{
        this.setState({
            fbflag: true
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
                
        <div class="container mx-auto" style={{marginTop:"100px"}}>
                    <div class="flex justify-center px-6 my-12">
                        <div class="w-full xl:w-3/4 lg:w-11/12 flex">
                            <div   class="w-full lg:w-1/2 bg-white p-5 rounded-lg lg:rounded-l-none" >
                                <div style={{marginTop:"100px"}}>
                                 <GoogleLogin
                                     clientId="520298412555-el7dpaev21s62g674raiccjqm8otrgmo.apps.googleusercontent.com"
                                      size="medium"
                                      onSuccess={this.responseGoogle}
                                      onFailure={this.responseGoogle}
                                       cookiePolicy={'single_host_origin'}
                                       buttonText="Sign up with Google"
                                     />
                                <div style={{marginTop:"30px"}}>
                                    <FacebookLogin
                                     appId="371065937316993"
                                     autoLoad={this.state.fbflag}
                                     fields="name,email"
                                     onClick={this.componentClicked}
                                     callback={this.responseFacebook}
                                     textButton="Sign up with FaceBook"


                                     />
                                </div>
                                </div>
                            </div>
                            <div   class="w-full lg:w-1/2 bg-white p-5 rounded-lg lg:rounded-l-none" >
                             <SignUp/>
                             </div>
                        </div>
                    </div>
                    <div class="form-group" style={{ "alignItems": "center" }}>
                                        {this.state.signupFailedError ? <span style={{ color: "red", "font-style": "oblique", "font-weight": "bold", "textAlign": "center" }}>SignUp Failed. Please try again.{this.state.msg}</span> : ''}
                                    </div>
                    </div>
                    </div>
            )
       }
}

   
export default Home;
