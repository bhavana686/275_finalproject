package com.cmpe275.models;

import java.util.List;

public class UserHistory {
	private long id;
	private String username;
	private String nickname;
	private List<TransferRequestForm> transactions;
	private int rating;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public List<TransferRequestForm> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransferRequestForm> transactions) {
		this.transactions = transactions;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
