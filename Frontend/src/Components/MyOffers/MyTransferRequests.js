import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import { Descriptions, Badge, Collapse, Button, message, Rate, Spin } from 'antd';
import moment from 'moment';
const { Panel } = Collapse;

class CounterRequests extends Component {
    constructor(props) {
        super(props);
        this.state = {
            request: [],
            userId: sessionStorage.getItem("id"),
            owner: false,
            loading: false
        }
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData = () => {
        this.setState({ loading: true })
        const { match: { params } } = this.props
        const offerId = params.id;

        let url = process.env.REACT_APP_BACKEND_URL + "/user/" + this.state.userId + "/requests";
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    request: response.data,
                    loading: false
                })
                console.log(response.data)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    request: [],
                    loading: false
                })
            });
    }

    acceptRequest = (id, requestId) => {
        this.setState({ loading: true })
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/" + id + "/request/" + requestId + "/accept";
        let body = {
            "userId": sessionStorage.getItem("id")
        }
        axios.defaults.withCredentials = true;
        axios.post(url, body)
            .then(response => {

                console.log(response.data)
                message.success("Accepted Request Successfully")
                this.fetchData();
            })
            .catch((error) => {
                this.fetchData();
                console.log(error);
                message.error("Error Accepting Request");
            });
    }

    declineRequest = (id, requestId) => {
        this.setState({ loading: true })
        let url = process.env.REACT_APP_BACKEND_URL + "/offer/" + id + "/request/" + requestId + "/decline";
        let body = {
            "userId": sessionStorage.getItem("id")
        }
        axios.defaults.withCredentials = true;
        axios.post(url, body)
            .then(response => {

                console.log(response.data)
                message.success("Declined Request Successfully")
                this.fetchData();
            })
            .catch((error) => {
                this.fetchData();
                console.log(error);
                message.error("Error Declining Request");
            });
    }

    render() {
        return (
            <div style={{ marginTop: "20px" }}>
                <Spin size="large" spinning={this.state.loading}>
                    <div style={{ fontSize: "20px", fontWeight: "600" }}>My Transfer Requests</div>
                    <div className="mx-32">
                        {this.state.request.map((counter, index) => {
                            let status = moment(new Date().getTime()).isAfter(parseInt(counter.expiry)) ? "Expired" : counter.status;
                            let expired = moment(new Date().getTime()).isAfter(parseInt(counter.expiry))
                            if (counter.status === "open" && !expired) {
                                return (
                                    <Collapse defaultActiveKey={['1']} className="my-4">
                                        <Panel header={"Request Id: " + counter.id + " Status: " + status} key="1"
                                            extra={!expired && counter.status === "open" && <div>
                                                <Button type="primary" onClick={() => this.acceptRequest(counter.offer.id, counter.id)}>Accept</Button>
                                                <Button type="primary" onClick={() => this.declineRequest(counter.offer.id, counter.id)} danger className="ml-2">Decline</Button>
                                            </div>}>
                                            <Descriptions title="Offer Details" bordered style={{ backgroundColor: "AppWorkspace" }} >
                                                <Descriptions.Item label="Counter For">{counter.offer.id}</Descriptions.Item>
                                                <Descriptions.Item label="Expires ">{moment(counter.expiry).format("LLLL")}</Descriptions.Item>
                                                <Descriptions.Item label="Original Amount">{counter.amountRequired}</Descriptions.Item>
                                                <Descriptions.Item label="Offer Amount">{parseInt(counter.amountAdjusted).toFixed(2)}</Descriptions.Item>
                                                <Descriptions.Item label="Requesting User Nick Name">{counter.user.nickname}</Descriptions.Item>
                                                <Descriptions.Item label="Source Currency">{counter.offer.sourceCurrency}</Descriptions.Item>
                                                <Descriptions.Item label="Source Country">{counter.offer.sourceCountry}</Descriptions.Item>
                                                <Descriptions.Item label="Destination Currency">{counter.offer.destinationCurrency}</Descriptions.Item>
                                                <Descriptions.Item label="Destination Country">{counter.offer.destinationCountry}</Descriptions.Item>
                                                <Descriptions.Item label="User Rating">
                                                    <Link to={"/user/" + counter.user.id} style={{ cursor: "pointer" }}>
                                                        <span>
                                                            <Rate defaultValue={counter.user.rating} disabled />&nbsp;{counter.user.rating === 0 ? "N/A" : counter.user.rating}
                                                        </span>
                                                    </Link>
                                                </Descriptions.Item>
                                            </Descriptions>
                                        </Panel>
                                    </Collapse>
                                )
                            }
                        })}
                    </div>
                </Spin>
            </div>
        );
    }
}

export default CounterRequests;