import React, { Component } from 'react';
import axios from 'axios';
import moment from 'moment';

class SystemFinancials extends Component {
    constructor(props) {
        super(props);
        this.state = {
            completedTransactionsCount: "",
            completedTransactionsTotal: "",
            pendingTransactionsCount: "",
            pendingTransactionsTotal: "",
            month: "",
            stats: []
        }
    }

    getTransactions = (e) => {
        let url = process.env.REACT_APP_BACKEND_URL + "/transactionHistory/systemFinancials?month=" + this.state.month;
        console.log(url);
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    stats: response.data,
                    status: 'success'
                })
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    stats: [],
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
        if (this.state.status == 'success' && this.state.stats.length == 0) {
            display = (<div style={{ color: "#1569E0", fontSize: "20px" }}> No System Financials found. </div>)
        }
        if (this.state.status == 'success' && this.state.stats.length != 0) {
            display = (<div class="row" style={{ marginTop: "15px" }}>
                <div class="col-md-2">
                </div>
                <div class="col-md-2">
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px", marginBottom: "10px" }}>
                        Completed Transactions
                    </div>
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px" }}>
                        {this.state.stats[0][0]}
                    </div>
                </div>
                <div class="col-md-2">
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px", marginBottom: "10px" }}>
                        Amount remitted in USD
                    </div>
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px" }}>
                        {parseFloat(this.state.stats[0][1]).toFixed(2)}
                    </div>
                </div>
                <div class="col-md-2">
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px", marginBottom: "10px" }}>
                        Pending Transactions
                    </div>
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px" }}>
                        {this.state.stats[1][0]}
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px", marginBottom: "10px" }}>
                        Service Fee in USD
                    </div>
                    <div class="row" style={{ color: "#1569E0", fontSize: "20px" }}>
                        {(parseFloat(this.state.stats[0][1])*0.05).toFixed(2)}
                    </div>
                </div>
                <div class="col-md-1">
                </div>

            </div>
            )
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

                <button style={{ marginBottom: "10px" }} onClick={this.getTransactions} class="btn btn-primary">Get System Financials</button>

                {display}

            </div>
        )
    }
}

export default SystemFinancials;