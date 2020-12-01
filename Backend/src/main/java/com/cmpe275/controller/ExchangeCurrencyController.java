package com.cmpe275.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.entity.Enum;
import com.cmpe275.service.UserService;
import com.cmpe275.service.ExchangeCurrencyService;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@CrossOrigin(origins="http://localhost:3000", allowedHeaders = "*",allowCredentials="true")
@RequestMapping(value = "/exchangeRate")
public class ExchangeCurrencyController {

	@Autowired
	private ExchangeCurrencyService ecService;

	@PostMapping
	public ResponseEntity<Object> addExchangeRate(HttpServletRequest request,
			@RequestParam("sourceCurrency") Enum.Currency sourceCurrency,
			@RequestParam("targetCurrency") Enum.Currency targetCurrency,
			@RequestParam("exchangeRate") Double exchangeRate) {
		return ecService.addRate(request, sourceCurrency, targetCurrency, exchangeRate);
	}

	@GetMapping
	public ResponseEntity<Object> getExchangeRate(HttpServletRequest request,
			@RequestParam("sourceCurrency") Enum.Currency sourceCurrency,
			@RequestParam("targetCurrency") Enum.Currency targetCurrency) {
		return ecService.getRate(request, sourceCurrency, targetCurrency);
	}

	@GetMapping("/getAll")
	public ResponseEntity<Object>  getAlllExchangeRate(HttpServletRequest request)
	{
		return ecService.getAllExchangeRate(request);
	}
	
	@PutMapping()
	public ResponseEntity<Object> updateExchangeRate(HttpServletRequest request,
			@RequestParam("sourceCurrency") Enum.Currency sourceCurrency,
			@RequestParam("targetCurrency") Enum.Currency targetCurrency,
			@RequestParam("exchangeRate") Double exchangeRate) {
		return ecService.updateRate(request, sourceCurrency, targetCurrency, exchangeRate);
	}
}