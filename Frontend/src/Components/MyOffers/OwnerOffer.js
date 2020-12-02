import React, { Component } from 'react';
import axios from 'axios';
import { Redirect } from 'react-router';
import { Link } from 'react-router-dom';
import { Card, Button, ButtonGroup } from 'react-bootstrap';
import { Descriptions, Badge } from 'antd';
import moment from 'moment';

class Offer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            offer: {},
            userId: sessionStorage.getItem("id"),
            owner: false
        }
    }

    componentDidMount() {
        const { match: { params } } = this.props
        const offerId = params.id;

        let url = process.env.REACT_APP_BACKEND_URL + "/offer/" + offerId;
        axios.defaults.withCredentials = true;
        axios.get(url)
            .then(response => {
                this.setState({
                    offer: response.data,
                    owner: sessionStorage.getItem("id") === response.data.postedBy.id.toString()
                })
                console.log(response.data)
            })
            .catch((error) => {
                console.log(error);
                this.setState({
                    offer: {}
                })
            });
    }

    render() {
        return (
            <div style={{ marginTop: "50px" }}>
                {this.state.offer ?
                    <div className="mx-32">
                        <Descriptions title="Offer Details" bordered style={{ backgroundColor: "AppWorkspace" }} >
                            <Descriptions.Item label="Id">{this.state.offer.id}</Descriptions.Item>
                            <Descriptions.Item label="Source Currency">{this.state.offer.sourceCurrency}</Descriptions.Item>
                            <Descriptions.Item label="Source Country">{this.state.offer.sourceCountry}</Descriptions.Item>
                            <Descriptions.Item label="Destination Currency">{this.state.offer.destinationCurrency}</Descriptions.Item>
                            <Descriptions.Item label="Destination Country">{this.state.offer.destinationCountry}</Descriptions.Item>
                            <Descriptions.Item label="Exchange Rate">{this.state.offer.exchangeRate}</Descriptions.Item>
                            <Descriptions.Item label="Expiry">{moment(this.state.offer.expiry).format('LLLL')}</Descriptions.Item>
                            <Descriptions.Item label="Status" span={3}>
                                <Badge status="processing" text={this.state.offer.status} />
                            </Descriptions.Item>
                            {!this.state.owner && this.state.offer.postedBy &&
                                <Descriptions.Item label="Created By">{this.state.offer.postedBy.nickname}</Descriptions.Item>
                            }
                            {!this.state.owner && this.state.offer.postedBy &&
                                <Descriptions.Item label="Created By">{this.state.offer.postedBy.username}</Descriptions.Item>
                            }
                        </Descriptions>
                    </div>
                    : ""
                }
            </div>
        );
    }
}

export default Offer;