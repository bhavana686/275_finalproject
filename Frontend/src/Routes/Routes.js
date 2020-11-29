import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Home from "../Components/Home";
import SignIn from "../Components/Authentication/SignIn";
import SignUp from "../Components/Authentication/SignUp";
import Landingpage from  "../Components/Landingpage";
import NavBar from '../Components/NavBar';

class Routes extends Component {
  render() {
    return (
      <div>
          <Route  path="/" component={NavBar} /> 
        <Route  path="/home" component={Home} /> 
        <Route path="/signin" component={SignIn} />
        <Route path="/signup" component={SignUp} />
        <Route path="/landingpage" component={Landingpage} />


        
      </div>
    );
  }
}

export default Routes;