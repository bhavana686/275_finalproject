import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { Button } from "react-bootstrap";


class Landingpage extends Component {
  constructor(props) {
    super(props);
    this.state = {
     redirect : "",
     userid : sessionStorage.getItem("nickname")
    }
    this.handleUpdate = this.handleUpdate.bind(this);

}

handleUpdate()
{
  console.log("in here")
  this.setState({
      redirect:"/addBankAccount/"
  })
}

render() {
  var displayform=null,redirectvar=null;
  if(this.state.redirect)
  {

      
          redirectvar = <Redirect push to={this.state.redirect} />;
        
  }
  displayform = 

    
          
              <div>
              <div class="form-group row" >
                          <div class="col-lg-3">        </div>
                          <div class="col-lg-4">
                          <div class="col-sm-10">
<h1>Welcome,{this.state.userid}</h1>
      {/* <div><button style={{backgroundColor:"blue",margin:"10px"}} onClick={this.handleaddnew}>Add New Bank Account</button></div> */}
      <Button  style={{backgroundColor:"blue",margin:"20px"}} onClick={this.handleUpdate}>
      Add New Bank Account
          </Button>
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
export default Landingpage;