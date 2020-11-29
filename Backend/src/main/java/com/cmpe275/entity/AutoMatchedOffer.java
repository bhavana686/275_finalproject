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
	
	@OneToMany
	private List<Offer> fullyFulfilledOffers;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Offer counteredOffer;
	
	@OneToOne(fetch = FetchType.LAZY)
	private CounterOffer counter;

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

	public List<Offer> getFullyFulfilledOffers() {
		return fullyFulfilledOffers;
	}

	public void setFullyFulfilledOffers(List<Offer> fullyFulfilledOffers) {
		this.fullyFulfilledOffers = fullyFulfilledOffers;
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

}
