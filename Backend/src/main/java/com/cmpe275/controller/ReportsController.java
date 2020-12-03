package com.cmpe275.controller;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.service.TransactionHistoryService;


@Controller
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping(value = "/transactionHistory")
public class ReportsController {

	@Autowired
	private TransactionHistoryService transactionHistoryService;
	
	@GetMapping
	public ResponseEntity<Object> getTransactionHistory(HttpServletRequest request) {
		return transactionHistoryService.getTransactions(request);
	}
	
	@GetMapping(value = "/systemFinancials")
	public ResponseEntity<Object> getSystemFinancilas(HttpServletRequest request) {
		return transactionHistoryService.getTotalFinancials(request);
	}
	
}
