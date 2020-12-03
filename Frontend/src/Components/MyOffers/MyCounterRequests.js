import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import { Descriptions, Badge, Collapse, Button, message, Rate } from 'antd';
import moment from 'moment';
const { Panel } = Collapse;

class CounterRequests extends Component {
    constructor(props) {
        super(props);
        this.state = {
            counter: [],
            userId: sessionStorage.getItem("id"),
            owner: false
        }
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData = () => {
        const { match: { params } } = this.props
        const offerId = params.id;

        let url = process.env.REACT_APP_BACKEND_URL + "/user/" + this.state.userId + "/counters";
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    counter: response.data
                })
                console.log(response.data)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    counter: []
                })
            });
    }

    acceptCounterRequest = (id) => {
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/counter/" + id + "/accept";
        let body = {
            "userId": sessionStorage.getItem("id")
        }
        axios.defaults.withCredentials = true;
        axios.post(url, body)
            .then(response => {
                console.log(response.data)
                message.success("Accepted Counter Successfully")
                this.fetchData();
            })
            .catch((error) => {
                console.log(error);
                message.error("Error Accepting Counter");
            });
    }

    declineCounterRequest = (id) => {
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/counter/" + id + "/decline";
        let body = {
            "userId": sessionStorage.getItem("id")
        }
        axios.defaults.withCredentials = true;
        axios.post(url, body)
            .then(response => {
                console.log(response.data)
                message.success("Declined Counter Successfully")
                this.fetchData();
            })
            .catch((error) => {
                console.log(error);
                message.error("Error Declining Counter");
            });
    }

    render() {
        return (
            <div style={{ marginTop: "20px" }}>
                <div style={{ fontSize: "20px", fontWeight: "600" }}>My Counter Offers</div>
                <div className="mx-32">
                    {this.state.counter.map((counter, index) => {
                        let status = moment(new Date().getTime()).isAfter(parseInt(counter.expiry)) ? "Expired" : counter.status;
                        let expired = moment(new Date().getTime()).isAfter(parseInt(counter.expiry))
                        return (
                            <Collapse defaultActiveKey={['1']} className="my-4">
                                <Panel header={"Request Id: " + counter.id + " Status: " + status} key="1"
                                    extra={!expired && counter.status === "open" && <div>
                                        <Button type="primary" onClick={() => this.acceptCounterRequest(counter.id)}>Accept</Button>
                                        <Button type="primary" onClick={() => this.declineCounterRequest(counter.id)} danger className="ml-2">Decline</Button>
                                    </div>}>
                                    <Descriptions title="Offer Details" bordered style={{ backgroundColor: "AppWorkspace" }} >
                                        <Descriptions.Item label="Counter For">{counter.counteredAgainst.id}</Descriptions.Item>
                                        <Descriptions.Item label="Expires ">{moment(counter.expiry).format("LLLL")}</Descriptions.Item>
                                        <Descriptions.Item label="Original Amount">{counter.originalAmount}</Descriptions.Item>
                                        <Descriptions.Item label="Counter Amount">{counter.counterAmount}</Descriptions.Item>
                                        <Descriptions.Item label="Request Created">{moment(counter.createdAt).format("LLLL")}</Descriptions.Item>
                                        <Descriptions.Item label="Nick Name">{counter.counteredBy.nickname}</Descriptions.Item>
                                        <Descriptions.Item label="User Rating">
                                            <Link to={"/user/" + counter.counteredBy.id} style={{ cursor: "pointer" }}>
                                                <span>
                                                    <Rate defaultValue={counter.counteredBy.rating} disabled />&nbsp;{counter.counteredBy.rating === 0 ? "N/A" : counter.counteredBy.rating}
                                                </span>
                                            </Link>
                                        </Descriptions.Item>
                                    </Descriptions>
                                </Panel>
                            </Collapse>
                        )
                    })}
                </div>
            </div>
        );
    }
}

export default CounterRequests;