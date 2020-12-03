import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Redirect } from "react-router";
import Avatar from "@material-ui/core/Avatar";
import "../App.css";

class NavBar extends Component {
  constructor(props) {
    super(props);
    this.state = {};
    this.handleLogout = this.handleLogout.bind(this);
  }
  handleLogout = () => {
    sessionStorage.removeItem("username");
    sessionStorage.removeItem("nickname");
    sessionStorage.removeItem("id");
  };

  render() {
    let navBar = null;
    if (
      sessionStorage.getItem("username") !== null
    ) {
      navBar = (
        <div class="flex ">
          <a href="/offer"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0"> Home</a>
          <a href="/offers"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0"> My Offers</a>
          <a href="/requests"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0"> My Transactions</a>
          <a href="/counters"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0"> My Counter Offers</a>
          <a href="/transactionHistory"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0"> Transaction History</a>
                  <a href="/bankAccount"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0"> My Bank Accounts</a>
          <a href="/signin" onClick={this.handleLogout}
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-red-400 lg:mt-0">Logout</a>
        </div>
      );
    } else {
      navBar = (
        <div class="flex ">
          <a href="/signin"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0">Sign In</a>
          <a href="/home"
            class="block text-md px-4 py-2 rounded text-blue-700 ml-2 font-bold hover:text-white mt-4 hover:bg-blue-400 lg:mt-0">Sign Up</a>
        </div>
      );
    }
    let redirectVar = null;
    // if (!sessionStorage.getItem("username")) redirectVar = <Redirect to="/signin" />

    return (
      <div>
        {redirectVar}
        <nav
          class="flex items-center justify-between flex-wrap bg-blue-200 py-6 lg:px-12 shadow border-solid ">
          <div class="flex justify-between lg:w-auto w-full lg:border-b-0 pl-6 pr-2 border-solid border-b-2 border-gray-300 ">
            <div class="flex items-center flex-shrink-0 text-gray-800 mr-16">
              <span class="font-semibold text-xl tracking-tight text-3xl font-bold"><Link to="/offer">DirectExchange</Link></span>
            </div>
          </div>
          <div class="menu w-full lg:block flex-grow lg:flex lg:items-right lg:w-auto lg:px-3 px-8">
            <div class="text-md font-bold text-blue-700 lg:flex-grow lg:items-right">
            </div>
            <div class="relative mx-auto text-gray-600 lg:block hidden">
              <input
                class="border-2 border-gray-300 bg-white h-10 pl-2 pr-8 rounded-lg text-sm focus:outline-none"
                type="search" name="search" placeholder="Search" />
              <button type="submit" class="absolute right-0 top-0 mt-3 mr-2">
                <svg class="text-gray-600 h-4 w-4 fill-current" xmlns="http://www.w3.org/2000/svg"
                  version="1.1" id="Capa_1" x="0px" y="0px"
                  viewBox="0 0 56.966 56.966"

                  width="512px" height="512px">
                  <path
                    d="M55.146,51.887L41.588,37.786c3.486-4.144,5.396-9.358,5.396-14.786c0-12.682-10.318-23-23-23s-23,10.318-23,23  s10.318,23,23,23c4.761,0,9.298-1.436,13.177-4.162l13.661,14.208c0.571,0.593,1.339,0.92,2.162,0.92  c0.779,0,1.518-0.297,2.079-0.837C56.255,54.982,56.293,53.08,55.146,51.887z M23.984,6c9.374,0,17,7.626,17,17s-7.626,17-17,17  s-17-7.626-17-17S14.61,6,23.984,6z" />
                </svg>
              </button>
            </div>
            {navBar}
          </div>

        </nav>
      </div>
    );
  }
}

export default NavBar;