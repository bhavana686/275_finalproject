package com.cmpe275.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class TransferRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "offer", referencedColumnName = "id")
	private Offer offer;

	@OneToOne(fetch = FetchType.LAZY)
	private Transaction transaction;

	@OneToOne
	private User user;

	@Enumerated(EnumType.STRING)
	private Enum.CounterOfferStatuses status = Enum.CounterOfferStatuses.open;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Timestamp expiry;

	private double amountRequired;

	private double amountAdjusted;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
