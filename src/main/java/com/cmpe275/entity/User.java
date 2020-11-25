package com.cmpe275.entity;

import javax.persistence.*;
import java.io.Serializable;


@SuppressWarnings("serial")
@Entity
public class User implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String username;
	private String nickname;
	private String signupType;
	private String password;
	

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
		this.username =  username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
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

	

}