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
			user = buildUserFromData(body);
			User u = userRepo.save(user);
			return new ResponseEntity<>(u, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	public  User buildUserFromData(JsonNode body) throws CustomException {
		User user = new User();
		try {
			if (userRepo.findByUsername(body.get("username").asText()).isPresent()) {
				throw new CustomException("user Already exists with given Email", HttpStatus.CONFLICT);
			}
			if (userRepo.findByNickname(body.get("nickname").asText()).isPresent()) {
				throw new CustomException("user Already exists with given Nick name", HttpStatus.CONFLICT);
			}
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
			
		} catch (CustomException e) {
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return user;
	}

	
}