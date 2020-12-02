package com.cmpe275.controller;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.service.EmailService;
import com.cmpe275.service.OfferService;
import com.fasterxml.jackson.databind.JsonNode;
import com.cmpe275.entity.Enum;

@Controller
@CrossOrigin(origins="http://localhost:3000", allowedHeaders = "*",allowCredentials="true")
@RequestMapping(value = "/offers")
public class OfferController {
	
	@Autowired
	private OfferService offerService;
	
	@Autowired
	private EmailService emailService;

	@GetMapping
	public ResponseEntity<Object> getOffers(HttpServletRequest request) {
		return offerService.getOffers(request);
	}
	
	@GetMapping("/filter")
	public ResponseEntity<Object> getFilteredOffers(HttpServletRequest request,
			@RequestParam("sourceCurrency") Enum.Currency sourceCurrency,
			@RequestParam("destinationCurrency") Enum.Currency destinationCurrency) {
		return offerService.getFilteredOffers(request,sourceCurrency,destinationCurrency);
	}
	
	@GetMapping("/emailapi")
	public ResponseEntity<Object> sendemail() {
		return emailService.sendEmail("prasannareddy699@gmail.com", "Welcome", "Good Morning!!");
	        
	}

	@PostMapping("/email")
	public ResponseEntity<Object> sendemail(HttpServletRequest request, @RequestBody JsonNode body) {
		return emailService.sendEmail(request,body);
	        
	}

}



