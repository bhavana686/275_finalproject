package com.cmpe275.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.User;
import com.cmpe275.models.UserShallowForm;
import com.cmpe275.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;



@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public  ResponseEntity<Object> signUp(HttpServletRequest req, JsonNode body) {
		User user;
		try {
			if ((userRepo.findByUsername(body.get("username").asText()).isPresent())) {
				return new ResponseEntity<>("user Already exists with given Email", HttpStatus.OK);
			}
			if ((userRepo.findByUsername(body.get("nickname").asText()).isPresent())) {
				return new ResponseEntity<>("nickname Already exists", HttpStatus.OK);
			}
			user = buildUserFromData(body);
			User u = userRepo.save(user);
			return new ResponseEntity<>(ConvertUserForm(u),HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			System.out.print(e);
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
		    Boolean isVerified = body.get("isVerified").asBoolean();
			if (isVerified != null)
				user.setIsVerified(isVerified);	
		 } 
		catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return user;
	}
	
	public  ResponseEntity<Object> signIn(HttpServletRequest req) {
		try {
			  String name = req.getParameter("username");
			  String type = req.getParameter("signupType");
			  Optional<User> u=userRepo.findByUsername(name);
			  if(u.isPresent() && u.get().getSignupType().equals(type))
			  {
				  Optional<User> us = userRepo.findByUsername(name);
				  return new ResponseEntity<>(us.get(), HttpStatus.OK);
				  
			  }
			else
			{
				return new ResponseEntity<>("no such username exists", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	public  ResponseEntity<Object> verifyMail(HttpServletRequest req,String username) {
		try {
			   System.out.println("username"+ username);
			   Optional<User> us = userRepo.getByUsername(username);
			   
			if(userRepo.findByUsername(username).isPresent()){
				Optional<User> u = userRepo.findByUsername(username);
				us.get().setIsVerified(true);
				User m=userRepo.save(us.get());
				return new ResponseEntity<>(ConvertUserForm(m), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>("no such username exists", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	public  UserShallowForm ConvertUserForm(User user) {
			   UserShallowForm  us= new  UserShallowForm ();
			   us.setId(user.getId());
			   us.setNickname(user.getNickname());
			   us.setUsername(user.getUsername());
			   us.setSignupType(us.getSignupType());
			   us.setIsVerified(us.getIsVerified());
			   us.setPassword(us.getPassword());
			   return us;
			  }
	
}