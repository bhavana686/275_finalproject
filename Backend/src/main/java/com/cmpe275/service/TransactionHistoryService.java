package com.cmpe275.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.models.*;
import com.cmpe275.repo.TransactionHistoryRepo;

@Service
public class TransactionHistoryService {
	
	@Autowired
	private TransactionHistoryRepo transactionHistoryRepo;
	
	public  ResponseEntity<Object> getTransactions(HttpServletRequest req) {
		try {
			long userid = (long) Long.parseLong(req.getParameter("userid"));
			int month = (int) Integer.parseInt(req.getParameter("month"));
			List<List<String>> transactionHistory = transactionHistoryRepo.getTransactionHistoryByMonth(userid,month);
			return new ResponseEntity<>(transactionHistory, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	public  ResponseEntity<Object> getTotalFinancials(HttpServletRequest req) {
		try {
			int month = (int) Integer.parseInt(req.getParameter("month"));
			List<List<String>> transactionHistory = transactionHistoryRepo.getTotalTransactionHistoryByMonth(month);
			return new ResponseEntity<>(transactionHistory, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

}
