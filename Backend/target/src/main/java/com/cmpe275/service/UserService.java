package com.cmpe275.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.User;
import com.cmpe275.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;



@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public  ResponseEntity<Object> signUp(HttpServletRequest req, JsonNode body) {
		User user;
		try {
			if ((userRepo.findByUsername(body.get("username").asText()).isPresent()) && (body.get("signupType").asText().equals("general"))) {
				return new ResponseEntity<>("user Already exists with given Email", HttpStatus.OK);
			}
			if (userRepo.findByUsername(body.get("username").asText()).isPresent()){
				Optional<User> us = userRepo.findByUsername(body.get("username").asText());
				return new ResponseEntity<>(us, HttpStatus.OK);
			}
			user = buildUserFromData(body);
			User u = userRepo.save(user);
			return new ResponseEntity<>(u,HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	public  User buildUserFromData(JsonNode body) throws CustomException {
		User user = new User();
		try{
			String username = body.get("username").asText();
			if (username != null)
	            user.setUsername(username);
			String nickname = body.get("nickname").asText();
			if (nickname != null)
				user.setNickname(nickname);
			String password = body.get("password").asText();
			if (password != null)
				user.setPassword(password);
			String signupType = body.get("signupType").asText();
			if (signupType != null)
				user.setSignupType(signupType);	
		 } 
		catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return user;
	}
	
	public  ResponseEntity<Object> signIn(HttpServletRequest req, String username) {
		try {
			if (userRepo.findByUsername("username").isPresent()){
				Optional<User> us = userRepo.findByUsername("username");
				return new ResponseEntity<>(us, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("no sunch username exists", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	
}