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



@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public  ResponseEntity<Object> signUp(HttpServletRequest req) {
		User user;
		try {
			user = buildUserFromData(req);
			User u = userRepo.save(user);
			return new ResponseEntity<>(u, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	public  User buildUserFromData(HttpServletRequest req) throws CustomException {
		User user = new User();
		try {
			if (userRepo.findByUsername(req.getParameter("username")).isPresent()) {
				throw new CustomException("user Already exists with given Email", HttpStatus.CONFLICT);
			}
			if (userRepo.findByNickname(req.getParameter("nickname")).isPresent()) {
				throw new CustomException("user Already exists with given Nick name", HttpStatus.CONFLICT);
			}
			String username = req.getParameter("username");
			if (username != null)
	            user.setUsername(username);
			String nickname = req.getParameter("nickname");
			if (nickname != null)
				user.setNickname(nickname);
			String password = req.getParameter("password");
			if (password != null)
				user.setPassword(password);
			String signupType = req.getParameter("signupType");
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