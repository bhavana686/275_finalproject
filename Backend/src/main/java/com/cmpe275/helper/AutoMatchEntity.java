package com.cmpe275.helper;

import java.util.List;

import com.cmpe275.entity.Offer;
import com.cmpe275.entity.Enum;

public class AutoMatchEntity {
	private double sum;
	private double difference;
	private List<AutoMatchRecommendationOffer> offers;
	private Enum.AutoMatchedOffers type;
	private boolean supportCounter;

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getDifference() {
		return difference;
	}

	public void setDifference(double difference) {
		this.difference = difference;
	}

	public List<AutoMatchRecommendationOffer> getOffers() {
		return offers;
	}

	public void setOffers(List<AutoMatchRecommendationOffer> offers) {
		this.offers = offers;
	}

	public Enum.AutoMatchedOffers getType() {
		return type;
	}

	public void setType(Enum.AutoMatchedOffers type) {
		this.type = type;
	}

	public boolean isSupportCounter() {
		return supportCounter;
	}

	public void setSupportCounter(boolean supportCounter) {
		this.supportCounter = supportCounter;
	}

}
