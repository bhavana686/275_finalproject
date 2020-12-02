import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import bcrypt from 'bcryptjs';
import HouseIcon from '@material-ui/icons/House';
import { CenterFocusStrong } from '@material-ui/icons';
import landingpage from "../Landingpage";
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import EditIcon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import {Edit} from '@material-ui/icons';

class CopyExchangeCurrency extends Component {
  constructor(props) {
    super(props);
    this.state = {
        sourceCurrency:"",
        targetCurrency:"",
        exchangeRate:"",
        useracc:[],
        edit: false,
    }
    this.handleedit = this.handleedit.bind(this);
    this.ChangeHandler = this.ChangeHandler.bind(this);
    
}
handleedit = () => {
    this.setState({
        edit: !this.state.edit

    })
}
submitEdit = (e) => {

}
    
componentDidMount() {
    let url = process.env.REACT_APP_BACKEND_URL+'/exchangeRate/getAll';
    console.log(url);
    axios.defaults.withCredentials = true;
    axios.get(url)
        .then(response => {
                this.setState({
                    useracc:response.data
                })
                console.log(this.state.useracc)
            
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                useracc:[]
            })
        });;
}


ChangeHandler = (event) => {
    this.setState({
        [event.target.name]: event.target.value
    })
}
    render() {
        let editform = null;
        let details=null;
        var displayform=null;
        displayform = (

            this.state.useracc.map(item => {
                return (
                    <div>
                    <div class="form-group row" paddingleft>
                                <div class="col-lg-3">        </div>
                                <div class="col-lg-4">
                         <Card style={{ height: "150px",width:"500px" ,marginBottom: "25px" }}>
                          <CardContent> 
            
                                <h5>Source Currency: {item.sourceCurrency}</h5><br></br>
                                <h5>Target Currency: {item.targetCurrency}</h5><br></br>
                                <h5>Exchange Rate:{item.exchangeRate}</h5><br></br>
                              
    
                           </CardContent>
                         </Card>

                    
                 </div>
                 </div></div>
                )
            }))
        

    return (
        <div style={{marginTop:"50px"}}>
         <div style={{marginLeft:"700px"}}>
         <Link to="/editExchangeRate"><button type="submit" class="btn btn-primary">Edit Exchange Rate</button></Link> &nbsp;
         
         <Link to="/addExchangeRate"><button type="submit" class="btn btn-primary">Add Exchange Rate</button></Link>
         </div>
            {displayform}
        </div>
        );
     }
}





export default CopyExchangeCurrency;