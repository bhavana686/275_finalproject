import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Home from "../Components/Home";
import SignIn from "../Components/Authentication/SignIn";
import SignUp from "../Components/Authentication/SignUp";
import Landingpage from  "../Components/Landingpage";
import NavBar from '../Components/NavBar';
import BankAccount from '../Components/Bank/BankAccount';
import AddBankAccount from '../Components/Bank/AddBankAccount';
import ExchangeCurrency from '../Components/ExchangeCurrency/ExchangeCurrency'
import AddExchangeRate from '../Components/ExchangeCurrency/AddExchangeRate'
import EditExchangeRate from '../Components/ExchangeCurrency/EditExchangeRate'


class Routes extends Component {
  render() {
    return (
      <div>
         <Route  path="/" component={NavBar} /> 
        <Route  path="/home" component={Home} /> 
        <Route path="/signin" component={SignIn} />
        <Route path="/signup" component={SignUp} />
        <Route path="/landingpage" component={Landingpage} />
        <Route path="/bankaccount" component={BankAccount} />
        <Route path="/exchangeCurrency" component={ExchangeCurrency} />
        <Route path="/addExchangeRate" component={AddExchangeRate} />
        <Route path="/editExchangeRate" component={EditExchangeRate} />
        <Route path="/addBankAccount" component={AddBankAccount} />





        
      </div>
    );
  }
}

export default Routes;