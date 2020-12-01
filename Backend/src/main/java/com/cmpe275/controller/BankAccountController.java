package com.cmpe275.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.BankAccount;
import com.cmpe275.service.BankAccountService;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@CrossOrigin(origins="*", allowedHeaders = "*",allowCredentials="true")
@RequestMapping(value = "/bank")
public class BankAccountController{

	@Autowired
	private BankAccountService bankAccountService;

	@PostMapping("/{id}")
	public ResponseEntity<Object> addAccount(HttpServletRequest request,@PathVariable("id") long Id,
			@RequestParam("primaryCurrency") Enum.Currency primaryCurrency,
			@RequestParam("country") Enum.Countries country,
			 @RequestBody JsonNode body) {
		return bankAccountService.addAccount(request,Id,primaryCurrency,country,body);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getAllAccounts(HttpServletRequest request, @PathVariable("id") long Id) {
		return bankAccountService.getAllAccounts(request,Id);
	}
	

}
