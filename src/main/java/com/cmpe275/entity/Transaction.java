package com.cmpe275.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToMany
	private List<Offer> offers;
	private Date date;

	public List<Offer> getOffers() {
		return offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}
}
