package com.cmpe275.models;

import java.sql.Timestamp;

import com.cmpe275.entity.Enum;

public class CounterOfferDeepForm {

	private long id;

	private OfferShallowForm counteredAgainst;

	private double originalAmount;
	private double counterAmount;

	private Enum.CounterOfferStatuses status;

	private Timestamp createdAt;

	private Timestamp expiry;

	private UserShallowForm counteredBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OfferShallowForm getCounteredAgainst() {
		return counteredAgainst;
	}

	public void setCounteredAgainst(OfferShallowForm counteredAgainst) {
		this.counteredAgainst = counteredAgainst;
	}

	public double getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public double getCounterAmount() {
		return counterAmount;
	}

	public void setCounterAmount(double counterAmount) {
		this.counterAmount = counterAmount;
	}

	public Enum.CounterOfferStatuses getStatus() {
		return status;
	}

	public void setStatus(Enum.CounterOfferStatuses status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getExpiry() {
		return expiry;
	}

	public void setExpiry(Timestamp expiry) {
		this.expiry = expiry;
	}

	public UserShallowForm getCounteredBy() {
		return counteredBy;
	}

	public void setCounteredBy(UserShallowForm counteredBy) {
		this.counteredBy = counteredBy;
	}

}
