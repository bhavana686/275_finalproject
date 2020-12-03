package com.cmpe275.models;

import java.sql.Timestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.Transaction;
import com.cmpe275.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TransferRequestDeepForm {
	
	private long id;
	private OfferDeepForm offer;
	private TransactionDeepForm transaction;
	private UserDeepForm user;
	private Enum.CounterOfferStatuses status;
	private Timestamp expiry;
	private double amountRequired;
	private double amountAdjusted;
	

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OfferDeepForm getOffer() {
		return offer;
	}

	public void setOffer(OfferDeepForm offer) {
		this.offer = offer;
	}

	public TransactionDeepForm getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionDeepForm transaction) {
		this.transaction = transaction;
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

	public  UserDeepForm getUser() {
		return user;
	}

	public void setUser(UserDeepForm userDeepForm) {
		this.user = userDeepForm;
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
