package com.cmpe275.models;

import java.sql.Timestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.cmpe275.entity.Enum;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TransactionHistory {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Timestamp date;
	
	@Enumerated(EnumType.STRING)
	private Enum.Currency source_currency;
	
	@Enumerated(EnumType.STRING)
	private Enum.Currency destination_currency;
	
	private double amount;
	private double exchanged_rate;
	private double destination_amount;
	private double service_fess;
}
