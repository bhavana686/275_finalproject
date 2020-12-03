package com.cmpe275.models;

import java.util.List;

import com.cmpe275.entity.Offer;
import com.cmpe275.entity.TransferRequest;

public class UserDeepForm {
	
	private long id;
	private String username;
	private String nickname;
	private List<OfferDeepForm> offers;
	private List<TransferRequestDeepForm> transferRequests;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public  List<OfferDeepForm> getOffers() {
		return offers;
	}

	public void setOffers(List<OfferDeepForm>  offer) {
		this.offers = offer;
	}
	public List<TransferRequestDeepForm> getTransferRequests() {
		return transferRequests;
	}

	public void setTransferRequests(List<TransferRequestDeepForm> transferRequests) {
		this.transferRequests = transferRequests;
	}


	



}
