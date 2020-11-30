import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { CenterFocusStrong } from '@material-ui/icons';
import landingpage from "../Landingpage";

class EditExchangeRate extends Component {
  constructor(props) {
    super(props);
    this.state = {
        sourceCurrency:"",
        targetCurrency:"",
        exchangeRate:"",
        useracc:"",
        flag:false
    }
    this.ChangeHandler = this.ChangeHandler.bind(this);
    this.registerExchange = this.registerExchange.bind(this);
}

ChangeHandler = (event) => {
    this.setState({
        [event.target.name]: event.target.value
    })
}

registerExchange = (event) => {
    event.preventDefault();
    console.log(this.state.sourceCurrency)
    let url = process.env.REACT_APP_BACKEND_URL+'/exchangeRate'+'?sourceCurrency='+this.state.sourceCurrency+'&targetCurrency='+this.state.targetCurrency+'&exchangeRate='+this.state.exchangeRate;
    console.log("call to back");
    console.log(url);
    axios.defaults.withCredentials = true;
    axios.put(url)
        .then(response => {
            console.log(response)
            if (response.status ===200) {
                this.setState({
                    useracc:response.data,
                    flag:true
                })
            }
            else{ 
                this.setState({
                useracc:"",
                flag:false
            })

            }
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                useracc:"",
                flag:false
            })
        });;
}

    render() {
        let redirectToDisplay = null;
        if (this.state.flag) redirectToDisplay = <Redirect to="/exchangeCurrency" />
     return (
        <div >
        {redirectToDisplay}
        <br />
        <form onSubmit={this.registerExchange}>
            <div class="container" >
                <div class="row">
                    <div class="col-lg-10 col-xl-9 mx-auto">
                        <div class="card card-signin flex-row my-5">

                            <div class="card-body" style={{marginBottom:"30px",marginRight:"300px"}}>
                                <h5> <b>EDIT CURRENCY EXCHANGE RATE</b></h5>
                            </div>

                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="sourceCurrency">Source Currency:</label>
                                <div class="col-sm-5">
                                    <input type="text" name="sourceCurrency" id="sourceCurrency" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                                <label class="control-label col-sm-2" for="targetCurrency">Target Currency:</label>
                                <div class="col-sm-5">
                                    <input type="text" name="targetCurrency" id="targetCurrency" onChange={this.ChangeHandler} class="form-control" required />
                                </div>
                            </div><br/><br/>
                            <div class="form-label-group">
                            <label class="control-label col-sm-2" for="exchangeRate">Exchange Rate:</label>
                            <div class="col-sm-5">
                                    <input type="text" name="exchangeRate" id="exchangeRate" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                            
                   
            <br /><br />
            <div class="form-group">
            <div class="col-sm-10">
            <button onClick={this.submitEvent} class="btn btn-primary" type="submit">Edit</button>&nbsp;

              
                <Link to="/exchangeCurrency"><button type="submit" class="btn btn-primary">Cancel</button></Link>
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

export default EditExchangeRate;