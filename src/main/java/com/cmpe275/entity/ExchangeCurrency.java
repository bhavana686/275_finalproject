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
public class ExchangeCurrency {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Enumerated(EnumType.STRING)
	private Enum.Currency targetCurrency;

	@Enumerated(EnumType.STRING)
	private Enum.Currency sourceCurrency;

	private double exchangeRate;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Enum.Currency getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(Enum.Currency sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}
	public Enum.Currency getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(Enum.Currency targetCurrency) {
		this.targetCurrency = targetCurrency;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate ) {
		this.exchangeRate =exchangeRate;
	}
	
	
}
