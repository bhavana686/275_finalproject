import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Home from "../Components/Home";
import SignIn from "../Components/Authentication/SignIn";
import SignUp from "../Components/Authentication/SignUp";
import Landingpage from "../Components/Landingpage";
import NavBar from '../Components/NavBar';
import BankAccount from '../Components/Bank/BankAccount';
import AddBankAccount from '../Components/Bank/AddBankAccount';
import ExchangeCurrency from '../Components/ExchangeCurrency/ExchangeCurrency'
import AddExchangeRate from '../Components/ExchangeCurrency/AddExchangeRate'
import EditExchangeRate from '../Components/ExchangeCurrency/EditExchangeRate'
import Offers from '../Components/Offers/Offers';
import Offer from '../Components/Offers/Offer';
import EditOffer from '../Components/Offers/EditOffer';
import CreateOffer from '../Components/Offers/createoffer';
import Verification from '../Components/Authentication/Verification'
import CopyExchangeCurrency from '../Components/ExchangeCurrency/CopyExchangeCurrency'
import SingleOfferOwnerView from "../Components/MyOffers/OwnerOffer";
import MyCounterRequests from "../Components/MyOffers/MyCounterRequests";
import MyTransferRequests from "../Components/MyOffers/MyTransferRequests";
import Profile from '../Components/Profile/Profile';
import TransactionHistory from '../Components/Reporting/TransactionHistory';
import UserProfile from '../Components/Profile/UserProfile';

class Routes extends Component {
  render() {
    return (
      <div>
        <Route path="/" component={NavBar} />
        <Route path="/home" component={Home} />
        <Route path="/signin" component={SignIn} />
        <Route path="/signup" component={SignUp} />
        <Route path="/landingpage" component={Landingpage} />
        <Route path="/exchangeCurrency" component={ExchangeCurrency} />
        <Route path="/copyExchangeCurrency" component={CopyExchangeCurrency} />
        <Route path="/addExchangeRate" component={AddExchangeRate} />
        <Route path="/editExchangeRate" component={EditExchangeRate} />
        <Route path="/addBankAccount" component={AddBankAccount} />
        <Route path="/offers" component={Offers} />
        <Route path="/editoffer/:id" component={EditOffer} />
        <Route path="/createoffer" component={CreateOffer} />
        <Route path="/verifyMail" component={Verification} />
        <Route path="/offer" exact component={Offer} />
        <Route path="/offer/:id" exact component={SingleOfferOwnerView} />
        <Route path="/counters" exact component={MyCounterRequests}/>
        <Route path="/requests" exact component={MyTransferRequests}/>
        <Route path="/bankAccount" component={BankAccount} />
        <Route path="/profile" component={Profile}/>
        <Route path="/transactionHistory" component={TransactionHistory}/>
        <Route path="/userProfile/:id" component={UserProfile}/>


      </div>
    );
  }
}

export default Routes;