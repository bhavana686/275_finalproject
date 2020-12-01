package com.cmpe275.helper;

import java.sql.Timestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Enum.OfferStatuses;

public class AutoMatchRecommendationOffer {
	public String nickname;

	public String username;

	public long id;

	private Enum.Countries sourceCountry;

	private Enum.Currency sourceCurrency;

	private double sourceAmount;

	private Enum.Countries destinationCountry;

	private Enum.Currency destinationCurrency;

	private double destinationAmount;

	private double exchangeRate;

	private Timestamp expiry;
	
	private boolean supportCounter;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public double getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(double sourceAmount) {
		this.sourceAmount = sourceAmount;
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

	public double getDestinationAmount() {
		return destinationAmount;
	}

	public void setDestinationAmount(double destinationAmount) {
		this.destinationAmount = destinationAmount;
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

	public boolean isSupportCounter() {
		return supportCounter;
	}

	public void setSupportCounter(boolean supportCounter) {
		this.supportCounter = supportCounter;
	}

}
