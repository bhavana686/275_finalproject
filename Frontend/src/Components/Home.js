import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Redirect } from 'react-router';
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

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
        signUpSuccessful: false,
        redirectToSignIn: false,
        passwordMatchError: false,
        signupFailedError: false,
    }
    this.responseGoogle=this.responseGoogle.bind(this);
    this.responseFacebook=this.responseFacebook.bind(this);
  }

  responseGoogle=(response)=>{
    let url = process.env.REACT_APP_BACKEND_URL+'/user';
    var data = {
        "username": response.profileObj.email,
        "password": response.profileObj.googleId,
        "nickname": response.profileObj.name,
        "signupType":"google"
    }
    axios.defaults.withCredentials = true;
    axios.post(url, data)
      .then(response => {
          if (response.status === 200) {
            sessionStorage.setItem("username", response.data.username);
            sessionStorage.setItem("id", response.data.id);
            sessionStorage.setItem("nickname", response.data.nickname);
              this.setState({
                  signUpSuccessful: true,
              })
          } 
           else {
              this.setState({
                  signUpSuccessful: false,
                  signupFailedError: true
              })
          }
      })
      .catch((error) => {
          this.setState({
              signUpSuccessful: false,
              signupFailedError: true
          })
        });;
  }
  responseFacebook=(response)=>{
  console.log(response);
   let url = process.env.REACT_APP_BACKEND_URL+'/user';
   var data = {
        "username": response.email,
        "password": response.id,
        "nickname": response.name,
        "signupType":"facebook"
    }
    axios.defaults.withCredentials = true;
    axios.post(url, data)
      .then(response => {
          console.log(response)
          if (response.status === 200) {
             sessionStorage.setItem("username", response.data.username);
             sessionStorage.setItem("id", response.data.id);
             sessionStorage.setItem("nickname", response.data.nickname);
              this.setState({
                  signUpSuccessful: true,
              })
          } else {
              this.setState({
                  signUpSuccessful: false,
                  signupFailedError: true
              })
          }
      })
      .catch((error) => {
          this.setState({
              signUpSuccessful: false,
              signupFailedError: true
          })
    
        });;


  }
  componentClicked=()=>{
    return(<div></div>)
  }
 
  render() {
    let redirectToSign = null;
    if (this.state.signUpSuccessful) redirectToSign = <Redirect to="/landingpage" />
    return (
        <div>
        {redirectToSign}
        <div class="container mx-auto" style={{marginTop:"100px"}}>
                    <div class="flex justify-center px-6 my-12">
                        <div class="w-full xl:w-3/4 lg:w-11/12 flex">
                            <div   class="w-full lg:w-1/2 bg-white p-5 rounded-lg lg:rounded-l-none" >
                                <div style={{marginTop:"100px"}}>
                                 <GoogleLoginBtn
                                     clientId="520298412555-el7dpaev21s62g674raiccjqm8otrgmo.apps.googleusercontent.com"
                                      size="medium"
                                      onSuccess={this.responseGoogle}
                                      onFailure={this.responseGoogle}
                                       cookiePolicy={'single_host_origin'}
                                     />
                                <div style={{marginTop:"30px"}}>
                                    <FacebookLoginBtn
                                     appId="371065937316993"
                                     autoLoad={true}
                                     fields="name,email"
                                     onClick={this.componentClicked}
                                     callback={this.responseFacebook}/>
                                </div>
                                </div>
                            </div>
                            <div   class="w-full lg:w-1/2 bg-white p-5 rounded-lg lg:rounded-l-none" >
                             <SignUp/>
                             </div>
                        </div>
                    </div>
                    </div>
                    </div>
            )
       }
}

   
export default Home;
