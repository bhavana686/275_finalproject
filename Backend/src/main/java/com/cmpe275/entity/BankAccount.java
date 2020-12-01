package com.cmpe275.entity;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class BankAccount {
	
	private String bankName;
	private String accountNumber;
	private String ownerName;
	private String ownerAddress;
	private String accountType;
	
	@Enumerated(EnumType.STRING)
	private Enum.Countries country;

	@Enumerated(EnumType.STRING)
	private Enum.Currency primaryCurrency;
	
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String  accountNumber) {
		this.accountNumber =  accountNumber;
	}
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerAddress() {
		return  ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this. ownerAddress =  ownerAddress;
	}
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType =  accountType;
	}
	public Enum.Countries getCountry() {
		return  country;
	}

	public void setCountry(Enum.Countries country) {
		this.country = country ;
	}
	public  Enum.Currency  getPrimaryCurrency() {
		return  primaryCurrency;
	}

	public void setPrimaryCurrency( Enum.Currency  primaryCurrency) {
		this.primaryCurrency = primaryCurrency ;
	}


}
