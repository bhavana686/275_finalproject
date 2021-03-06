import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import landingpage from '../Landingpage';
import FacebookLogin from 'react-facebook-login';
import GoogleLogin from 'react-google-login';

class SignIn extends Component {
  constructor(props) {
    super(props);
    this.state = {
      invalidCredentials: false,
      email: "",
      password: "",
      invalidEmail: false,
      verification:false,
      fbflag:false,
    }
    this.authenticateUser = this.authenticateUser.bind(this);
    this.emailChangeHandler = this.emailChangeHandler.bind(this);
    this.passwordChangeHandler = this.passwordChangeHandler.bind(this);
    this.validateCredentials = this.validateCredentials.bind(this);
    this.componentClicked = this.componentClicked.bind(this);
  }
  componentClicked=()=>{
    this.setState({
        fbflag: true
    })

}

  authenticateUser = (event) => {
    event.preventDefault();
    console.log(this.state.email);
    let url = process.env.REACT_APP_BACKEND_URL +'/user?username='+this.state.email+'&signupType='+"general";
    console.log(url)
    axios.defaults.withCredentials = true;
    axios.get(url)
      .then(response => {
        console.log(response);
        if (response.data.username!=null && response.data.signupType=="general" && response.data.isVerified) {
          if (bcrypt.compareSync(this.state.password, response.data.password)) {
            sessionStorage.setItem("username", this.state.email);
            sessionStorage.setItem("id", response.data.id);
            sessionStorage.setItem("nickname", response.data.nickname);
            this.setState({
              invalidCredentials: false
            })
          }
          else if(!response.data.isVerified)
          {
             this.setState({
              msg:"account not verified",
             })
          }
           else {
            this.setState({
              invalidCredentials: true,
              msg:"password mismatch",
            })
          }
        } 
        else if(response.data.username!=null) {
          this.setState({
            invalidCredentials: true,
            msg:"account not verified ",
          })
        }
        else{
          this.setState({
            invalidCredentials: true,
            msg:response.data
          })

        }
      })
      .catch((error) => {
        console.log(error)
        this.setState({
          invalidCredentials: true
        })
      });;
  }

  responseGoogle = (response) => {
    console.log(this.state.email);
    let url = process.env.REACT_APP_BACKEND_URL +'/user?username='+response.profileObj.email+'&signupType='+"google";
    axios.defaults.withCredentials = true;
    axios.get(url)
      .then(response => {
        console.log(response);
        if (response.data.username!=null && response.data.isVerified) {
            sessionStorage.setItem("username", response.data.username);
            sessionStorage.setItem("id", response.data.id);
            sessionStorage.setItem("nickname", response.data.nickname);
            this.setState({
               invalidCredentials: false
            })
          }
          else if(response.data.username!=null && !response.data.isVerified)
          {
             this.setState({
               invalidCredentials: true,
               msg:"account not verified",
             })
          }
         else{
          this.setState({
            invalidCredentials: true,
             msg:response.data
           })
           }
      })
      .catch((error) => {
        console.log(error)
        this.setState({
          invalidCredentials: true
        })
      });;
  }
  responseFacebook = (response) => {
		console.log(response);
    let url = process.env.REACT_APP_BACKEND_URL +'/user?username='+response.email+'&signupType='+"facebook";
    axios.defaults.withCredentials = true;
    axios.get(url)
      .then(response => {
        console.log(response);
        if (response.data.username!=null && response.data.isVerified) {
            sessionStorage.setItem("username", response.data.username);
            sessionStorage.setItem("id", response.data.id);
            sessionStorage.setItem("nickname", response.data.nickname);
            this.setState({
               invalidCredentials: false
            })
          }
          else if(response.data.username!=null && !response.data.isVerified)
          {
             this.setState({
              invalidCredentials: true,
              msg:"account not verified",
             })
          }
         else{
          this.setState({
            invalidCredentials: true,
             msg:response.data
           })
           }
      })
      .catch((error) => {
        console.log(error)
        this.setState({
          invalidCredentials: true
        })
      });;
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
    this.setState({
      password: event.target.value
    })
  }

  validateCredentials = (event) => {
    if (!this.state.invalidEmail && this.state.password !== "") return false
    else return true
  }

  render() {
    let homev = null;
    console.log(new Date().toISOString().slice(0, 10))
    if (sessionStorage.getItem("username") !== null) {
      homev = <Redirect to="/offer" />
    }
    return (
      
      <div>
        {homev}
                    
        <div class="container mx-auto" style={{marginTop:"100px" }}>
          <div class="flex justify-center px-6 my-12">
            <div class="w-full xl:w-3/4 lg:w-11/12 flex">
             
              <div class="w-full lg:w-1/8 bg-white p-5 ">
                <div className="mb-8">
                  <h3 class="pt-4 text-4xl text-center font-bold">Direct Exchange</h3>
                  <div class="row">
                  <div class="col-lg-6">
                  <div style={{marginTop:"100px"}}>
                                 <GoogleLogin
                                     clientId="520298412555-el7dpaev21s62g674raiccjqm8otrgmo.apps.googleusercontent.com"
                                      size="medium"
                                      onSuccess={this.responseGoogle}
                                      onFailure={this.responseGoogle}
                                       cookiePolicy={'single_host_origin'}
                                       buttonText="Sign in with Google"
                                     />
                                <div style={{marginTop:"30px"}}>
                                    <FacebookLogin
                                     appId="371065937316993"
                                     autoLoad={this.state.fbflag}
                                     fields="name,email"
                                     onClick={this.componentClicked}
                                     callback={this.responseFacebook}
                                     textButton="Sign in with FaceBook"
                                     />
                                </div>
                                </div>
                  </div>
                  <div class="col-lg-6">
                  <div class="text-center mt-10">
                    <h2 class="text-2xl tracking-tight font-bold">
                      Sign In
                    </h2>
                  </div>
                  <div class="flex justify-center my-2 mx-4 md:mx-0">
                    <form class="w-full max-w-xl bg-white rounded-lg shadow-md p-6" >
                      <div class="flex flex-wrap -mx-3 mb-6">
                        <div class="w-full md:w-full px-3 mb-6">
                          <label class="block tracking-wide text-gray-700 text-md font-bold mb-2" for='Password'>User Name</label>
                          <input class="text-md appearance-none rounded w-full py-2 px-3 text-gray-700 bg-gray-100 leading-tight focus:outline-none focus:shadow-outline h-12 border border-gray-400 rounded-lg" type='email' onChange={this.emailChangeHandler} required />
                          {this.state.invalidEmail && <p class="text-sm text-bold italic text-red-500">Please enter a valid Email.</p>}
                        </div>
                        <div class="w-full md:w-full px-3 mb-6">
                          <label class="block tracking-wide text-gray-700 text-md font-bold mb-2" for='Password'>Password</label>
                          <input class="text-md appearance-none rounded w-full py-2 px-3 text-gray-700 bg-gray-100 leading-tight focus:outline-none focus:shadow-outline h-12 border border-gray-400 rounded-lg" type='password' onChange={this.passwordChangeHandler} required />
                        </div>
                        <div class="w-full flex items-center justify-between px-3 mb-3 ">
                          <label for="remember" class="flex items-center w-1/2">
                            <input type="checkbox" name="" id="" class="mr-1 bg-white shadow" />
                            <span class="text-sm text-gray-700 pt-1 ml-2">Remember Me</span>
                          </label>
                          <div class="w-1/2 text-right">
                            <a href="#" class="text-blue-500 text-md tracking-tight">Forgot your password?</a>
                          </div>
                        </div>
                        <div class="w-full md:w-full px-3 mb-6">
                          <button
                            class="appearance-none block w-full bg-blue-600 text-gray-100 font-bold border border-gray-200 rounded-lg py-3 px-3 leading-tight hover:bg-blue-500 focus:outline-none focus:bg-white focus:border-gray-500 mb-1"
                            type="submit"
                            onClick={this.authenticateUser}
                          >
                            Sign in
                          </button>
                         
                        </div>
                        <div class="mt-4 w-full border-t border-gray-400" style={{ textAlign: "center" }}>
                          <div class="text-md mt-6">
                            <a href="/Home" class="text-blue-500">
                              New User? Register an Account
                            </a>
                          </div>
                        </div>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
            {this.state.invalidCredentials && <p class="text-md text-bold italic text-red-500"> Please try again {this.state.msg}</p>}

          </div>
        </div>
        
      </div>
      </div>
        
      </div>
    )
  }
}

export default SignIn;