package com.cmpe275.models;

public class UserShallowForm {
	private long id;
	private String username;
	private String nickname;
	private String signupType;
	private String password;
	private Boolean isVerified;
	private int rating;

	public UserShallowForm() {

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getSignupType() {
		return signupType;
	}

	public void setSignupType(String type) {
		this.signupType = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String type) {
		this.password = type;
	}
	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean type) {
		this.isVerified =type ;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
