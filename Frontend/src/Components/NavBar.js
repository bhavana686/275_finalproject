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
   if (sessionStorage.getItem("username") !== null) {
      navBar = (
        <ul class="nav navbar-nav navbar-right">
          <li>
            <Link
              to="/signin"
              onClick={this.handleLogout}
              style={{ color: "black" }}
            >
              <span class="glyphicon glyphicon-log-out"></span> Logout
            </Link>
          </li>
        </ul>
      );
    } else {
      navBar = (
        <ul class="nav navbar-nav navbar-right">
          <li>
            <Link to="/signin" style={{ color: "black" }}>
              <span class="glyphicon glyphicon-log-in"></span> Log In
            </Link>
          </li>
          <li>
            <Link to="/home" style={{ color: "black" }}>
              <span class="glyphicon glyphicon-user"></span> Sign Up
            </Link>
          </li>
        </ul>
      );
    }
    let redirectVar = null;
    // if (!sessionStorage.getItem("email")) redirectVar = <Redirect to="/signin" />
    return (
      <div>
        {redirectVar}
        <nav
          class="navbar navbar-dark bg-dark"
          style={{
            backgroundColor: "#bdd4e7",
            backgroundImage: "linear-gradient(315deg, #bdd4e7 0%, #8693ab 74%)",
            borderRadius: "0px",
            padding: "0px",
            margin: "0px",
            paddingTop: "3px",
            paddingBottom: "3px",
            boxShadow: "0 2px 5px rgba(0,0,0,0.3)",
            color: "white",
          }}
        >
          <div class="container-fluid">
            <div class="navbar-header" style={{ display: "inline" }}>
              <b
                class="navbar-brand"
                style={{
                  color: "black",
                  display: "inline",
                  paddingRight: "0px",
                }}
              >
                Direct Exchange
              </b>
            </div>
            <ul class="nav navbar-nav"></ul>
            {navBar}
          </div>
        </nav>
      </div>
    );
  }
}

export default NavBar;