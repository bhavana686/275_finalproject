package com.cmpe275.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.Offer;
import com.cmpe275.service.ExchangeRateService;
import com.cmpe275.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(value = "/offer")
public class ExchangeRateController {

	@Autowired
	private ExchangeRateService exchangeRateService;

//	@PostMapping
//	public ResponseEntity<Object> createExchangeOffer(HttpServletRequest request) {
//		return exchangeRateService.createExchangeOffer(request);
//	}
//	
	
   @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOffer(@RequestBody Offer offer) {

      System.out.println("User: {}"+ offer.getAmount());
      System.out.println("User: {}"+ offer.getExchangeRate());
      return exchangeRateService.createOffer(offer);
    }
	   
	@PutMapping("/{id}")
	public ResponseEntity<String> updateOffer(@RequestBody Offer offer, @PathVariable("id") long id) {
		
		return exchangeRateService.updateOffer(offer,id);
	}
	

 
}