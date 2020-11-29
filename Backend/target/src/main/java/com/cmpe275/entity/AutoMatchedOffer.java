package com.cmpe275.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AutoMatchedOffer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long originalOffer;
	private long[] splittedOffers;
	private long counteredOffer;
	private long counterId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOriginalOffer() {
		return originalOffer;
	}

	public void setOriginalOffer(long originalOffer) {
		this.originalOffer = originalOffer;
	}

	public long[] getSplittedOffers() {
		return splittedOffers;
	}

	public void setSplittedOffers(long[] splittedOffers) {
		this.splittedOffers = splittedOffers;
	}

	public long getCounteredOffer() {
		return counteredOffer;
	}

	public void setCounteredOffer(long counteredOffer) {
		this.counteredOffer = counteredOffer;
	}

	public long getCounterId() {
		return counterId;
	}

	public void setCounterId(long counterId) {
		this.counterId = counterId;
	}

}
