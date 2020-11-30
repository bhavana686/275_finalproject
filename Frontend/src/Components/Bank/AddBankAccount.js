import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { CenterFocusStrong } from '@material-ui/icons';
import landingpage from "../Landingpage";

class AddBankAccount extends Component {
  constructor(props) {
    super(props);
    this.state = {
     bankName:"",
     accountNumber:"",
	 ownerName:"",
     ownerAddress:"",
     accountType:"",
     country:"",
     primaryCurrency:"",
     useracc:"",
    }
    this.changeHandler = this.changeHandler .bind(this);
    this.registerUser = this.registerUser.bind(this);
}

changeHandler = (event) => {
    this.setState({
        [event.target.name]: event.target.value
    })
}

registerUser = (event) => {
    event.preventDefault();
    let id=sessionStorage.getItem("id");
    let url = process.env.REACT_APP_BACKEND_URL+'/bank/'+id+'?primaryCurrency='+this.state.primaryCurrency+'&country='+this.state.country;
    console.log(url);
    var data = {
        bankName:this.state.bankName,
        accountNumber:this.state.accountNumber,
        ownerName:this.state.ownerName,
        ownerAddress:this.state.ownerAddress,
        accountType:this.state.accountType,
    }
    console.log("call to back");
    axios.defaults.withCredentials = true;
    axios.post(url, data)
        .then(response => {
            console.log(response)
            if (response.status ===200) {
                this.setState({
                    useracc:response.data
                })
            }
            else{ 
                this.setState({
                useracc:""
            })

            }
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                useracc:""
            })
        });;
}

    render() {
    return (
        <div >
        <br />
        <form onSubmit={this.registerUser}>
            <div class="container" >
                <div class="row">
                    <div class="col-lg-10 col-xl-9 mx-auto">
                        <div class="card card-signin flex-row my-5">

                            <div class="card-body" style={{marginBottom:"30px",marginRight:"300px"}}>
                                <h5> <b>ADD BANK ACCOUNT</b></h5>
                            </div>

                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="bankName">Bank name:</label>
                                <div class="col-sm-5">
                                    <input type="text" name="bankName" id="bankName" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="accountNumber">Account Number:</label>
                                <div class="col-sm-5">
                                    <input type="text" name="accountNumber" id="accountNumber" onChange={this.ChangeHandler} class="form-control" required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                            <label class="control-label col-sm-2" for=" ownerName">Owner Name:</label>
                            <div class="col-sm-5">
                                    <input type="text" name="ownerName" id="ownerName" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="ownerAddress">Owner Address:</label>
                                <div class="col-sm-5">
                                <input type="text" name="ownerAddress" id="ownerAddress" onChange={this.ChangeHandler} class="form-control" required />
                        </div>
                        </div><br /><br />
                        <div class="form-label-group">
                            <label class="control-label col-sm-2" for="accountType">Account Type</label>
                            <div class="col-sm-5">
                            <input type="text" name="accountType"  id="accountType" onChange={this.ChangeHandler} class="form-control" required />
                        </div>
                    </div><br /><br />
                    
                    <div class="form-label-group">
                        <label class="control-label col-sm-2" for="country">Country:</label>
                        <div class="col-sm-5">
                        <input type="text" name="country" id="country" onChange={this.ChangeHandler} class="form-control" required />
                    </div><br /><br />
                    <div class="form-label-group">
                            <label class="control-label col-sm-2" for="primaryCurrency">Primary Currency:</label>
                            <div class="col-sm-5">
                            <input type="text" name="primaryCurrency"  id="primaryCurrency" onChange={this.ChangeHandler} class="form-control" required />
                        </div>
                    </div><br /><br />

                </div>
            <br /><br />
            <div class="form-group">
            <div class="col-sm-10">
            <button onClick={this.submitEvent} class="btn btn-primary" type="submit">Add</button>&nbsp;

              
                <Link to="/landingpage"><button type="submit" class="btn btn-primary">Cancel</button></Link>
                </div>

            </div>
            
           
</div>
</div>
</div>
</div>

</form>
</div>



                  
    )
}
}

export default AddBankAccount;