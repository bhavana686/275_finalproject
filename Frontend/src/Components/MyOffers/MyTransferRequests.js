import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import { Descriptions, Badge, Collapse, Button } from 'antd';
import moment from 'moment';
const { Panel } = Collapse;

class CounterRequests extends Component {
    constructor(props) {
        super(props);
        this.state = {
            request: [],
            userId: sessionStorage.getItem("id"),
            owner: false
        }
    }

    componentDidMount() {
        const { match: { params } } = this.props
        const offerId = params.id;

        let url = process.env.REACT_APP_BACKEND_URL + "/user/" + this.state.userId + "/requests";
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    request: response.data
                })
                console.log(response.data)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    courequestnter: []
                })
            });
    }

    render() {
        return (
            <div style={{ marginTop: "20px" }}>
                <div style={{ fontSize: "20px", fontWeight: "600" }}>My Counter Offers</div>
                <div className="mx-32">
                    {this.state.request.map((counter, index) => {
                        let status = moment(new Date().getTime()).isAfter(parseInt(counter.expiry)) ? "Expired" : counter.status;
                        let expired = moment(new Date().getTime()).isAfter(parseInt(counter.expiry))
                        return (
                            <Collapse defaultActiveKey={['1']} className="my-4">
                                <Panel header={"Request Id: " + counter.id + " Status: " + status} key="1"
                                    extra={!expired && <div>
                                        <Button type="primary">Accept</Button>
                                        <Button type="primary" danger className="ml-2">Decline</Button>
                                    </div>}>
                                    <Descriptions title="Offer Details" bordered style={{ backgroundColor: "AppWorkspace" }} >
                                        <Descriptions.Item label="Counter For">{counter.offer.id}</Descriptions.Item>
                                        <Descriptions.Item label="Expires ">{moment(counter.expiry).format("LLLL")}</Descriptions.Item>
                                        <Descriptions.Item label="Original Amount">{counter.amountRequired}</Descriptions.Item>
                                        <Descriptions.Item label="Offer Amount">{counter.amountAdjusted}</Descriptions.Item>
                                        <Descriptions.Item label="Nick Name">{counter.user.nickname}</Descriptions.Item>
                                        <Descriptions.Item label="User Rating">N/A</Descriptions.Item>
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