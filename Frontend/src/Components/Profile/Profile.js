import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { Button } from "react-bootstrap";


class Profile extends Component {
  constructor(props) {
    super(props);
    this.state = {
     redirect : "",
     userid : sessionStorage.getItem("username"),
     nickname : sessionStorage.getItem("nickname"),
      success: "",
     error : "true",
     invalid : false
    }
    // this.handleUpdate = this.handleUpdate.bind(this);
this.handleAdd = this.handleAdd.bind(this);
// this.nicknameChangeHandler = this.nicknameChangeHandler.bind(this);
this.validateCredentials = this.validateCredentials.bind(this);
this.handleChange = this.handleChange.bind(this);

}



// nicknameChangeHandler = (event) => {
    
//     if (/[A-Za-z0-9]+$/.test(event.target.value)) {
//       this.setState({
//         invalid: false,
//         nickname: event.target.value
//       })
//     } else {
//       this.setState({
//         invalid: true,
//         nickname: event.target.value
//       })
//     }
//   }

  
  handleChange = (e) => {
    console.log("e", e.target.name, " ", e.target.value);

    this.setState({
      [e.target.name]: e.target.value,
    });
  };

  validateCredentials = (nname) => {

    
    if (!this.state.invalid) return true
    else return false
  }


handleAdd = (e) => {
    e.preventDefault();

    let url = process.env.REACT_APP_BACKEND_URL+"/user";

    this.setState({
        invalid: true,
        success : ""
             })

const data = 
{
    nickname :this.state.nickname,
    userid : this.state.userid
}

if (/[A-Za-z0-9]+$/.test(this.state.nickname)) {
    console.log("valid")
    this.setState({
      invalid: false,
      error :  "false",
      nickname:this.state.nickname
    })
  } else {
      console.log("invalid")
    this.setState({
      invalid: true,
      error :  "true",
      nickname : this.state.nickname
    })
  }

console.log(data)
    //set the with credentials to true
    axios.defaults.withCredentials = true;
    //make a post request with the user data

    if(!this.state.invalid)
    {
        console.log("form is valid")
        console.log("into the reqyuest");
    axios
      .put(url,data)
      .then((response) => {
        console.log(response);
        if (response.data === "error") {
          console.log("error");
          this.setState({
            showRegistrationError: true,
          });
        } else if (response.data === "Success") {
          this.setState({
            success : "Successfully Updated the nickname"
          });
        }
      })
      .catch((ex) => {
        this.setState({
          showRegistrationError: true,
        });
      });
    }
   
  };


 


render() {
  var displayform=null,redirectvar=null;
  if(this.state.success)
  {

      
          redirectvar = <div><h1>Successfully Updated</h1></div>;
        
  }
  displayform = 

    
          
              <div>
 

              <div class="form-group row" >
                          <div class="col-lg-3">        </div>
                          <div class="col-lg-4">
                          <div class="col-sm-10">
                          <div className="form-group">
            <label className="col-form-label w-100 text-left">
             UserName
            </label>
                <input
                  type="text"
                  
                  name="username"
                  className="form-control"
                  
                  defaultValue={this.state.userid}
                  readonly = "true"
                />
              </div>

    <div className="form-group">
            <label className="col-form-label w-100 text-left">
             NickName
            </label>
                <input
                  type="text"
                  
                  name="nickname"
                  className="form-control"
                  
                  onChange={this.handleChange}
                  defaultValue={this.state.nickname}
                />
                                          {this.state.invalid && <p class="text-sm text-bold italic text-red-500">Please enter a valid Username.</p>}

              </div>

              

         <Button  style={{backgroundColor:"blue",margin:"20px"}} onClick={this.handleAdd}>
Update Profile          </Button>
      {/* <div><button style={{backgroundColor:"blue",margin:"10px"}} onClick={this.handleaddnew}>Add New Bank Account</button></div> */}
      {/* <Button  style={{backgroundColor:"blue",margin:"20px"}} onClick={this.handleUpdate}>
      Add New Bank Account
          </Button> */}
</div>
                          

           </div>
           </div></div>
          
      
  

return (
  <div style={{marginTop:"50px"}}>
               {redirectvar}

      {displayform}
  </div>
  );
}
}
export default Profile;