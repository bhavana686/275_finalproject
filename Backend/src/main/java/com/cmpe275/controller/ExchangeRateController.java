package com.cmpe275.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Offer;
import com.cmpe275.service.ExchangeRateService;
import com.cmpe275.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping(value = "/offer")
public class ExchangeRateController {

	@Autowired
	private ExchangeRateService exchangeRateService;

	@PostMapping("/postoffer")
	public ResponseEntity<String> createOffer(HttpServletRequest request, @RequestBody JsonNode body) {

		System.out.println("in create offer");
		return exchangeRateService.createOffer(request, body);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateOffer(@RequestBody JsonNode body, @PathVariable("id") long id) {

		return exchangeRateService.updateOffer(body, id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> displayrecords(@PathVariable("id") long id) {

		return exchangeRateService.getOffersByUserId(id);
	}

	@GetMapping("currentoffer/{id}")
	public ResponseEntity<Object> displayoffer(@PathVariable("id") long id) {

		return exchangeRateService.getofferbyofferid(id);
	}

}