package com.cmpe275.models;

import java.sql.Timestamp;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.User;
import com.cmpe275.entity.Enum.OfferStatuses;

public class OfferDeepForm {
	
	private long id;
	private Enum.Countries sourceCountry;
	private Enum.Currency sourceCurrency;
	private Enum.Countries destinationCountry;
	private Enum.Currency destinationCurrency;
	private Enum.OfferStatuses status = OfferStatuses.open;
	private double amount;
	private double exchangeRate;
	private Timestamp expiry;
	private UserShallowForm postedBy;
	private boolean usePrevailingRate;
	private boolean allowCounterOffers;
	private boolean allowSplitExchanges;
	private boolean isCounter;
	private double transactedAmount;
	private boolean fullyFulfilled;
	
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
	public Enum.Countries getDestinationCountry() {
		return destinationCountry;
	}
	public void setDestinationCountry(Enum.Countries destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	public Enum.Currency getDestinationCurrency() {
		return destinationCurrency;
	}
	public void setDestinationCurrency(Enum.Currency destinationCurrency) {
		this.destinationCurrency = destinationCurrency;
	}
	public Enum.OfferStatuses getStatus() {
		return status;
	}
	public void setStatus(Enum.OfferStatuses status) {
		this.status = status;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Timestamp getExpiry() {
		return expiry;
	}
	public void setExpiry(Timestamp expiry) {
		this.expiry = expiry;
	}
	public UserShallowForm getPostedBy() {
		return postedBy;
	}
	public void setPostedBy(UserShallowForm postedBy) {
		this.postedBy = postedBy;
	}
	public boolean isUsePrevailingRate() {
		return usePrevailingRate;
	}
	public void setUsePrevailingRate(boolean usePrevailingRate) {
		this.usePrevailingRate = usePrevailingRate;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
