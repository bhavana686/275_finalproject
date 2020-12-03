package com.cmpe275.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.TransferRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TransactionShallowForm {
	
	private long id;
	private List<TransferRequest> requests;
	private Timestamp expiry;
	private double amount;
	private Enum.CounterOfferStatuses status;

	public Enum.CounterOfferStatuses getStatus() {
		return status;
	}

	public void setStatus(Enum.CounterOfferStatuses status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public List<TransferRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<TransferRequest> requests) {
		this.requests = requests;
	}

	public Timestamp getExpiry() {
		return expiry;
	}

	public void setExpiry(Timestamp expiry) {
		this.expiry = expiry;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}


