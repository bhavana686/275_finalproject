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

class UserProfile extends Component {
  constructor(props) {
    super(props);
    this.state = {
       
        user:"",
        mailSent: false,
        msgText:"",

        
    }
    this.ChangeHandler = this.ChangeHandler.bind(this);
    this.submitEvent=this.submitEvent.bind(this);
    
}
    
componentDidMount() {
    const { match: { params } } = this.props
    const userId = params.id;


    let url = process.env.REACT_APP_BACKEND_URL+'/user/'+userId;
    console.log(url);
    axios.defaults.withCredentials = true;
    axios.get(url)
        .then(response => {
                this.setState({
                    user:response.data
                })
                console.log(this.state.useracc)
            
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                user:""
            })
        });;
}
submitEvent=(e)=>
{
    let url = process.env.REACT_APP_BACKEND_URL+'/offers/email';
        var data = {
            "sendto":this.state.user.username,
            "subject":"direct exchnage email verification",
            "message": this.state.msgText
        }
        axios.post(url, data)
        .then(response => {
            if (response.status==200) {
                this.setState({
                    mailSent: true,
                    
                })
            }
            else
            {
                this.setState({
                    mailSent: false,
                })
            }
        })
        .catch((error) => {
            console.log(error);
            this.setState({
                mailSent: false,
                
            })
        });;
    }



ChangeHandler = (event) => {
    this.setState({
        [event.target.name]: event.target.value
    })
}
    render() {
        var displayform=null;
        displayform = (

            
               
                    <div>
                    <div class="form-group row" >
                                <div class="col-lg-3">        </div>
                                <div class="col-lg-4">
                         <Card style={{ height: "100px",width:"500px" ,textAlign:"left" }}>
                          <CardContent> 
                          <div class="row">
                            <div class="col-lg-6"> User Name</div>
                            <div class="col-lg-6">  {this.state.user.username}</div>
                            </div>
                            <div class="row">
                            <div class="col-lg-6">  Nick name</div>
                            <div class="col-lg-6">  {this.state.user.nickname}</div>
                            </div>
    
                           </CardContent>
                         </Card>

                    
                 </div>
                 </div></div>
                )
           
        

    return (
        <div style={{marginTop:"50px"}}>
            {displayform}
            
            <form onSubmit={this.submitEvent}>
            <div class="container" >
                <div class="row">
                    <div class="col-lg-10 col-xl-9 mx-auto">
                        <div class="card card-signin flex-row my-5">

                            

                            <div class="form-label-group">
                                
                                <div class="col-sm-9" style={{height:"100%", width:"100%"}}>
                                    <input type="textarea" name="msgText" id="msgText" placeHolder=" Enter your Message" onChange={this.ChangeHandler} class="form-control"  required />
                                </div>
                            </div><br/><br/>
                           
            
            <div class="form-group">
            <div class="col-sm-10">
              <button onClick={this.submitEvent} class="btn btn-primary" type="submit">Send Message</button>&nbsp;

              
                <Link to="/"><button type="submit" class="btn btn-primary">Cancel</button></Link>
                </div>

            </div>
            
           
</div>
</div>
</div>
</div>

</form>



        </div>
        );
     }
}





export default UserProfile;