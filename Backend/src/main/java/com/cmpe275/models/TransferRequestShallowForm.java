package com.cmpe275.models;

import java.sql.Timestamp;

import com.cmpe275.entity.Enum;

public class TransferRequestShallowForm {
	private long id;

	private Enum.CounterOfferStatuses status;

	private Timestamp expiry;

	private double amountRequired;

	private double amountAdjusted;
	
	private OfferShallowForm offer;

	private UserShallowForm user;
	

	public OfferShallowForm getOffer() {
		return offer;
	}

	public void setOffer(OfferShallowForm offer) {
		this.offer = offer;
	}

	public UserShallowForm getUser() {
		return user;
	}

	public void setUser(UserShallowForm user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Enum.CounterOfferStatuses getStatus() {
		return status;
	}

	public void setStatus(Enum.CounterOfferStatuses status) {
		this.status = status;
	}

	public Timestamp getExpiry() {
		return expiry;
	}

	public void setExpiry(Timestamp expiry) {
		this.expiry = expiry;
	}

	public double getAmountRequired() {
		return amountRequired;
	}

	public void setAmountRequired(double amountRequired) {
		this.amountRequired = amountRequired;
	}

	public double getAmountAdjusted() {
		return amountAdjusted;
	}

	public void setAmountAdjusted(double amountAdjusted) {
		this.amountAdjusted = amountAdjusted;
	}
	
}
