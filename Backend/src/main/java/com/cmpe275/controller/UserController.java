
package com.cmpe275.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.entity.Enum;
import com.cmpe275.service.EmailService;
import com.cmpe275.service.OfferResponseService;
import com.cmpe275.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@CrossOrigin(origins="http://localhost:3000", allowedHeaders = "*",allowCredentials="true")
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService  emailService;
	
	@Autowired
	private OfferResponseService offerService;

	@PostMapping()
	public ResponseEntity<Object> userSignup(HttpServletRequest request, @RequestBody JsonNode body) {
		return userService.signUp(request,body);
	}
	
	@GetMapping
	public ResponseEntity<Object> userSignin(HttpServletRequest request) {
		return userService.signIn(request);
	}
	
	@GetMapping("/verifyMail")
	public ResponseEntity<Object> verifyEmail(HttpServletRequest request,@RequestParam("username") String username) {
		return userService.verifyMail(request, username);
	}
	
	@GetMapping("/{id}/counters")
	public ResponseEntity<Object> fetchCounterOffers(HttpServletRequest request, @PathVariable("id") long userId) {
		return offerService.fetchCounterOffers(request, (long) userId);
	}
	
	@GetMapping("/{id}/requests")
	public ResponseEntity<Object> fetchTransferRequests(HttpServletRequest request, @PathVariable("id") long userId) {
		return offerService.fetchTransferRequests(request, (long) userId);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> fetchUserData(HttpServletRequest request, @PathVariable("id") long userId) {
		System.out.println(userId);
		return offerService.fetchUserData(request, (long) userId);
	}

	@PutMapping
	public ResponseEntity<Object> updateuserprofile(HttpServletRequest request, @RequestBody JsonNode body) {
		return userService.updateProfile(request, body);
	}
}
