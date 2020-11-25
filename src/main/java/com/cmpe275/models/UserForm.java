
package com.cmpe275.models;


import com.cmpe275.entity.User;
import lombok.AllArgsConstructor;
import net.bytebuddy.dynamic.scaffold.MethodGraph;

import java.util.LinkedList;
import java.util.List;

/*
 * Model Structure for Sponsor Deep Form
 */
public class UserForm {
	private long id;
	private String username;
	private String nickname;
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public UserForm() {
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
}
