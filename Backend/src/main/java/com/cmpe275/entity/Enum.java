package com.cmpe275.entity;

public class Enum {
	public static enum Countries {
		Europe, UK, India, China, US
	}

	public static enum Currency {
		EUR, GBP, INR, RMB, USD
	}

	public static enum OfferStatuses {
		open, fulfilled, expired
	}
	
	public static enum CounterOfferStatuses {
		open, accepted, declined, expired
	}
}
