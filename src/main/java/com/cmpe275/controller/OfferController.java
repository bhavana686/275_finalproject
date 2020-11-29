package com.cmpe275.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.service.OfferService;

@Controller
@RequestMapping(value = "/offers")
public class OfferController {
	
	@Autowired
	private OfferService offerService;

	@GetMapping
	public ResponseEntity<Object> getOffers(HttpServletRequest request) {
		return offerService.getOffers(request);
	}
	
	@GetMapping("/filter")
	public ResponseEntity<Object> getFilteredOffers(HttpServletRequest request) {
		return offerService.getFilteredOffers(request);
	}
	
}
