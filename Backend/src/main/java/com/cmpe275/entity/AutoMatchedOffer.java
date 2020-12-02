package com.cmpe275.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class AutoMatchedOffer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Offer originalOffer;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Offer fullyFulfilledOffer;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Offer counteredOffer;
	
	@OneToOne(fetch = FetchType.LAZY)
	private CounterOffer counter;
	
	private Enum.AutoMatchTypes type;
	
	private Enum.AutoMatchOffersState status = Enum.AutoMatchOffersState.open;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Offer getOriginalOffer() {
		return originalOffer;
	}

	public void setOriginalOffer(Offer originalOffer) {
		this.originalOffer = originalOffer;
	}

	public Offer getCounteredOffer() {
		return counteredOffer;
	}

	public void setCounteredOffer(Offer counteredOffer) {
		this.counteredOffer = counteredOffer;
	}

	public CounterOffer getCounter() {
		return counter;
	}

	public void setCounter(CounterOffer counter) {
		this.counter = counter;
	}
	
	public Offer getFullyFulfilledOffer() {
		return fullyFulfilledOffer;
	}

	public void setFullyFulfilledOffer(Offer fullyFulfilledOffer) {
		this.fullyFulfilledOffer = fullyFulfilledOffer;
	}

	public Enum.AutoMatchTypes getType() {
		return type;
	}

	public void setType(Enum.AutoMatchTypes type) {
		this.type = type;
	}

	public Enum.AutoMatchOffersState getStatus() {
		return status;
	}

	public void setStatus(Enum.AutoMatchOffersState status) {
		this.status = status;
	}

}
