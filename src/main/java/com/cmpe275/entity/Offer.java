package com.cmpe275.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.cmpe275.entity.Enum.OfferStatuses;

@Entity
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Enumerated(EnumType.STRING)
	private Enum.Countries sourceCountry;

	@Enumerated(EnumType.STRING)
	private Enum.Currency sourceCurrency;

	private double amount;
	
	@Enumerated(EnumType.STRING)
	private Enum.Countries destinationCountry;

	@Enumerated(EnumType.STRING)
	private Enum.Currency destinationCurrency;

	@Enumerated(EnumType.STRING)
	private Enum.OfferStatuses status = OfferStatuses.open;

	private double exchangeRate;
	private boolean usePrevailingRate = false;
	private Timestamp expiry;
	private boolean allowCounterOffers = true;
	private boolean allowSplitExchanges = true;

	@ManyToOne(fetch = FetchType.LAZY)
	private User postedBy;

	@OneToOne(fetch = FetchType.LAZY)
	private Transaction fulfilledBy;

	private boolean isCounter = false;

	private double transactedAmount;
	
	private boolean fullyFulfilled;
	
	private boolean isEditable = true;
	
	private boolean display = true;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Enum.Countries getSourceCountry() {
		return sourceCountry;
	}

	public void setSourceCountry(Enum.Countries sourceCountry) {
		this.sourceCountry = sourceCountry;
	}

	public Enum.Currency getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(Enum.Currency sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public Enum.OfferStatuses getStatus() {
		return status;
	}

	public void setStatus(Enum.OfferStatuses status) {
		this.status = status;
	}

	public Enum.Currency getDestinationCurrency() {
		return destinationCurrency;
	}

	public void setDestinationCurrency(Enum.Currency destinationCurrency) {
		this.destinationCurrency = destinationCurrency;
	}

	public Enum.Countries getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(Enum.Countries destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public boolean isUsePrevailingRate() {
		return usePrevailingRate;
	}

	public void setUsePrevailingRate(boolean usePrevailingRate) {
		this.usePrevailingRate = usePrevailingRate;
	}

	public Timestamp getExpiry() {
		return expiry;
	}

	public void setExpiry(Timestamp expiry) {
		this.expiry = expiry;
	}

	public boolean isAllowCounterOffers() {
		return allowCounterOffers;
	}

	public void setAllowCounterOffers(boolean allowCounterOffers) {
		this.allowCounterOffers = allowCounterOffers;
	}

	public boolean isAllowSplitExchanges() {
		return allowSplitExchanges;
	}

	public void setAllowSplitExchanges(boolean allowSplitExchanges) {
		this.allowSplitExchanges = allowSplitExchanges;
	}

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}

	public Transaction getFulfilledBy() {
		return fulfilledBy;
	}

	public void setFulfilledBy(Transaction fulfilledBy) {
		this.fulfilledBy = fulfilledBy;
	}

	public boolean isCounter() {
		return isCounter;
	}

	public void setCounter(boolean isCounter) {
		this.isCounter = isCounter;
	}

	public double getTransactedAmount() {
		return transactedAmount;
	}

	public void setTransactedAmount(double transactedAmount) {
		this.transactedAmount = transactedAmount;
	}

	public boolean isFullyFulfilled() {
		return fullyFulfilled;
	}

	public void setFullyFulfilled(boolean fullyFulfilled) {
		this.fullyFulfilled = fullyFulfilled;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

}
