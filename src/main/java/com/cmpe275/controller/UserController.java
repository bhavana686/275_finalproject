
package com.cmpe275.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.cmpe275.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@CrossOrigin(origins="http://localhost:3000", allowedHeaders = "*",allowCredentials="true")
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping()
	public ResponseEntity<Object> userSignup(HttpServletRequest request, @RequestBody JsonNode body) {
		return userService.signUp(request,body);
	}
	
	@GetMapping
	public ResponseEntity<Object> userSignin(HttpServletRequest request) {
		return userService.signIn(request);
	}
	

}
