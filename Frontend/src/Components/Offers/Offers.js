import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import {Card,Button,ButtonGroup} from 'react-bootstrap';

import Moment from 'moment';


class Offers extends Component {
  constructor(props) {
    super(props);
    this.state = {
        sourceCurrency:"",
        targetCurrency:"",
        exchangeRate:"",
        offers:[],
        edit: false,
        id: sessionStorage.getItem("id")
    }
    this.handleUpdate = this.handleUpdate.bind(this);
    this.ChangeHandler = this.ChangeHandler.bind(this);
    
}

handleUpdate(id){
    console.log("in here")
    this.setState({
        redirect:`/editoffer/${id}`
    })
}

    
componentDidMount() {
    let url = process.env.REACT_APP_BACKEND_URL+"/offer/"+this.state.id;
    console.log(url);
    axios.defaults.withCredentials = true;
    axios.get(url)
        .then(response => {
                this.setState({
                    offers:response.data
                })
                console.log(this.state.offers)
            
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                offers:[]
            })
        });;
}


ChangeHandler = (event) => {
    this.setState({
        [event.target.name]: event.target.value
    })
}
    render() {
        let redirectVar = null
        if (this.state.redirect) {
            redirectVar = <Redirect push to={this.state.redirect} />;
          }
        let editform = null;
        let details=null;
        var displayform=null;


        
        displayform = this.state.offers.map((msg) => {
                return (
                    <div>
                &nbsp;
                    <Card style={{width:"50%",marginLeft:"100px",height:"80%",backgroundColor:"white"}}>
                
                    <Card.Body>
                      <Card.Text>
                
                      <p style={{color:"black"}}>Source Currency: {msg.sourceCurrency}</p> 
                      <p style={{color:"black"}}>Destination Currency : {msg.destinationCurrency}</p>
                      <p style={{color:"black"}}>Exchange Rate : {msg.exchangeRate}</p>
                      <p style={{color:"black"}}>Amount : {msg.amount}</p>
                      <p style={{color:"black"}}>Status : {msg.status}</p>

                      <p style={{color:"black"}}>Expiry Date :  {Moment(msg.expiry).format('YYYY-MM-DD')}</p>
                      <span hidden={!msg.editable}>
                      <div><Button onClick={(e)=> this.handleUpdate(msg.id)}>Edit Offer</Button></div></span>
                      </Card.Text>
                    </Card.Body>
                  </Card>
</div>
                )})


            
        

    return (
        <div style={{marginTop:"50px"}}>
         <div style={{marginLeft:"700px"}}>
         {redirectVar}
         <Link to="/createoffer"><button type="submit" class="btn btn-primary">Create Offer</button></Link>
         
         </div>
            {displayform}
        </div>
        );
     }
}





export default Offers;