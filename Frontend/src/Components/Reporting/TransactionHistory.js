import React, { Component } from 'react';
import axios from 'axios';
import moment from 'moment';

class TransactionHistory extends Component {
    constructor(props) {
        super(props);
        this.state = {
            completedTransactions: [],
            month: "",
            date: "",
            source_currency: "",
            amount: "",
            destination_currency: "",
            exchanged_rate: "",
            destination_amount: "",
            service_fee: "",
            status: ""
        }

    }

    getTransactions = (e) => {
        let url = process.env.REACT_APP_BACKEND_URL + "/transactionHistory?userid=" + sessionStorage.getItem("id") + "&month=" + this.state.month;
        console.log(url);
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    completedTransactions: response.data,
                    status: 'success'
                })
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    completedTransactions: [],
                    status: 'failure'
                })
            });;
    }

    monthChangeHandler = (e) => {
        console.log(e.target.value)
        this.setState({
            month: e.target.value
        })
    }

    let


    render() {
        let display = null
        if (this.state.status=='success'&&this.state.completedTransactions.length == 0) {
             display = (<div style={{ color: "#1569E0", fontSize: "20px" }}> No Transaction History found. </div>)
        }
        if (this.state.status=='success'&&this.state.completedTransactions.length != 0) {
             display = (<table class="table">
                <thead>
                    <tr>
                        <th scope="col">Date</th>
                        <th scope="col">Source Currency</th>
                        <th scope="col">Source Amount</th>
                        <th scope="col">Exchange Rate</th>
                        <th scope="col">Destination Currency</th>
                        <th scope="col">Destination Amount</th>
                        <th scope="col">Service Fee</th>
                    </tr>
                </thead>
                <tbody>
                    {this.state.completedTransactions.map(transaction => (
                        <tr>
                            <th scope="row">{moment(transaction[0]).format('LLLL')}</th>
                            <th scope="row">{transaction[1]}</th>
                            <th scope="row">{parseFloat(transaction[2]).toFixed(2)}</th>
                            <th scope="row">{transaction[4]}</th>
                            <th scope="row">{transaction[3]}</th>
                            <th scope="row">{parseFloat(transaction[5]).toFixed(2)}</th>
                            <th scope="row">{parseFloat(transaction[6]).toFixed(2)}</th>
                        </tr>
                    ))}

                </tbody>
            </table>)
        }
        return (
            <div>
                <div class="form-group">
                    <div style={{ color: "#1569E0", fontSize: "20px", display: "inline", marginTop: "10px" }}>Select Month : </div>
                    <div style={{ display: "inline" }}>
                        <select class="form-group form-control" onChange={this.monthChangeHandler} style={{ width: "10%", display: "inline", marginTop: "10px" }} id="month" name="month">
                            <option value='1'>January</option>
                            <option value="2">February</option>
                            <option value="3">March</option>
                            <option value="4">April</option>
                            <option value="5">May</option>
                            <option value="6">June</option>
                            <option value="7">July</option>
                            <option value="8">August</option>
                            <option value="9">September</option>
                            <option value="10">October</option>
                            <option value="11">November</option>
                            <option value="12">December</option>
                        </select>
                    </div>
                </div>

                <button style={{ marginBottom: "10px" }} onClick={this.getTransactions} class="btn btn-primary">Get Transaction History</button>

                {display}

            </div>
        )
    }
}

export default TransactionHistory;