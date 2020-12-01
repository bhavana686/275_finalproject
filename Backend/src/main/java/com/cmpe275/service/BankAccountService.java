package com.cmpe275.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.BankAccount;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Enum.Currency;
import com.cmpe275.entity.Enum.Countries;
import com.cmpe275.entity.User;
import com.cmpe275.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;



@Service
public class BankAccountService {
	
	@Autowired
	private UserRepo userRepo;
	
	public  ResponseEntity<Object> addAccount(HttpServletRequest req, long id,Enum.Currency primaryCurrency,Enum.Countries country,JsonNode body) {
		User new_user;
		try {
			Optional<User> user = userRepo.getById(id);
			if (!user.isPresent()) {
				return new ResponseEntity<>("user Not present", HttpStatus.NOT_FOUND);
			} else {
				new_user=buildBankAccountFromData(req, primaryCurrency,country,body,user.get());
				//User s = userRepo.save(new_user);
				return new ResponseEntity<>(new_user, HttpStatus.OK);
			}
		 }
		 catch (CustomException e) {
				return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
			} catch (Exception e) {
				return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
			}
		}
			
	public  User buildBankAccountFromData(HttpServletRequest req, Currency primaryCurrency, Countries country,JsonNode body, User u) throws CustomException {
		try{
			
			
			List<BankAccount> list=u.getBankAccounts();
			System.out.print("list    " + list);
			
			if(list==null)
			{
				list = new ArrayList();
				
			}
			BankAccount ba=new BankAccount();

			String bankName = body.get("bankName").asText();
			if (bankName != null)
	            ba.setBankName(bankName);
			String accountNumber = body.get("accountNumber").asText();
			if (accountNumber != null)
				ba.setAccountNumber(accountNumber);
			String ownerName = body.get("ownerName").asText();
			if (ownerName != null)
				ba.setOwnerName(ownerName);
			String ownerAddress = body.get("ownerAddress").asText();
			if (ownerAddress != null)
				ba.setOwnerAddress(ownerAddress);	
			String accountType = body.get("accountType").asText();
			if (accountType!= null)
				ba.setAccountType(accountType);	
			if(primaryCurrency!=null)
				ba.setPrimaryCurrency(primaryCurrency);	
			if(country!=null)
				ba.setCountry(country);	
			
			list.add(ba);
			u.setBankAccounts(list);
			System.out.println("jj"+ u.getBankAccounts());
			userRepo.save(u);
			
			
			return u;
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	public  ResponseEntity<Object> getAllAccounts(HttpServletRequest req,long Id) {
		List<BankAccount> l;
		try {
			 Optional<User> user = userRepo.findById(Id);
			if (!user.isPresent()) {
				throw new CustomException("user doesn't exists", HttpStatus.BAD_REQUEST);
			} else {
				  l=user.get().getBankAccounts();
				return new ResponseEntity<>(l, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	
}
//u.setId(oldUser.getId());
//u.setId(oldUser.getId());
//u.setCounterOffers(oldUser.getCounterOffers());
//u.setIsVerified(oldUser.getIsVerified());
//u.setSignupType(oldUser.getSignupType());
//u.setNickname(oldUser.getNickname());
//u.setUsername(oldUser.getUsername());
//u.setPassword(oldUser.getPassword());
//u.setBankAccounts(oldUser.getBankAccounts());
