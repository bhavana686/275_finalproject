package com.cmpe275.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.cmpe275.entity.Enum;

@Entity
public class CounterOffer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "counteredAgainst", referencedColumnName = "id")
	private Offer counteredAgainst;

	private double originalAmount;
	private double counterAmount;
	
	@Enumerated(EnumType.STRING)
	private Enum.CounterOfferStatuses status = Enum.CounterOfferStatuses.open;
	
	private long counteredBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Offer getCounteredAgainst() {
		return counteredAgainst;
	}

	public void setCounteredAgainst(Offer counteredAgainst) {
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

	public long getCounteredBy() {
		return counteredBy;
	}

	public void setCounteredBy(long counteredBy) {
		this.counteredBy = counteredBy;
	}

}