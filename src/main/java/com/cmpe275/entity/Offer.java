package com.cmpe275.entity;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private enum Countries {
		Europe, UK, India, China, US
	}

	private enum Currency {
		EUR, GBP, INR, RMB, USD
	}

	private enum OfferStatuses {
		open, fulfilled, expired
	}

	private Countries sourceCountry;
	private Currency sourceCurrency;
	private double amount;
	private Countries destinationCountry;
	private Currency destinationCurrency;
	private double exchangeRate;
	private boolean usePrevailingRate;
	private Date expiry;
	private boolean allowCounterOffers = true;
	private boolean allowSplitExchanges = true;

	@ManyToOne(fetch = FetchType.LAZY)
	private User postedBy;

	private OfferStatuses status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Countries getSourceCountry() {
		return sourceCountry;
	}

	public void setSourceCountry(Countries sourceCountry) {
		this.sourceCountry = sourceCountry;
	}

	public Currency getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(Currency sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
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

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
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

	public OfferStatuses getStatus() {
		return status;
	}

	public void setStatus(OfferStatuses status) {
		this.status = status;
	}

	public Currency getDestinationCurrency() {
		return destinationCurrency;
	}

	public void setDestinationCurrency(Currency destinationCurrency) {
		this.destinationCurrency = destinationCurrency;
	}

	public Countries getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(Countries destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

}
