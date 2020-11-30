package com.cmpe275.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.repo.ExchangeCurrencyRepo;
import com.cmpe275.repo.OffersRepo;
import com.cmpe275.repo.UserRepo;
import com.cmpe275.entity.*;
import com.cmpe275.entity.Enum;


@Service
public class ExchangeCurrencyService {

	@Autowired
	private ExchangeCurrencyRepo exchangeCurrencyRepo;

	public  ResponseEntity<Object> addRate(HttpServletRequest req, Enum.Currency source, Enum.Currency target,double rate)
	{
		try{
			 ExchangeCurrency c=new  ExchangeCurrency(); 
			 Enum.Currency sourceCurrency=source;
			 if(sourceCurrency!=null)
			 {
				 c.setSourceCurrency(sourceCurrency);
			 }
			 Enum.Currency targetCurrency=target;
			 if(targetCurrency!=null)
			 {
				 c.setTargetCurrency(targetCurrency);
			 }
			 double exchangeRate=rate;
			 Double d=new Double(exchangeRate);
			 if(d!=null)
			 {
				 c.setExchangeRate(exchangeRate);
			 }
			 ExchangeCurrency ec = exchangeCurrencyRepo.save(c);
			 return new ResponseEntity<>(ec, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	public  ResponseEntity<Object> getAllExchangeRate(HttpServletRequest req){
		try {
		    List<ExchangeCurrency> ec =exchangeCurrencyRepo.findAll();
			if (ec.isEmpty()) {
				return new ResponseEntity<>("no exchnage rate exists", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(ec, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	public  ResponseEntity<Object> getRate(HttpServletRequest req, Enum.Currency source, Enum.Currency target) {
		try {
			if (source == null)
				throw new CustomException("source currency  is Invalid", HttpStatus.NOT_FOUND);
			if (target == null)
				throw new CustomException("target currency  is Invalid", HttpStatus.NOT_FOUND);
			 Enum.Currency sourceCurrency=source;
			 Enum.Currency targetCurrency=target;
		    Optional<ExchangeCurrency> ec =exchangeCurrencyRepo.findBySourceCurrencyAndTargetCurrency(sourceCurrency,targetCurrency);
		    		
			if (!ec.isPresent()) {
				return new ResponseEntity<>("no exchnage rate exists", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(ec, HttpStatus.OK);
			}
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	public  ResponseEntity<Object> updateRate(HttpServletRequest req, Enum.Currency source, Enum.Currency target, double rate) {
		
		try {
			if (source == null)
				throw new CustomException("source currency  is Invalid", HttpStatus.NOT_FOUND);
			if (target == null)
				throw new CustomException("target currency  is Invalid", HttpStatus.NOT_FOUND);
			 Enum.Currency sourceCurrency=source;
			 Enum.Currency targetCurrency=target;
		    Optional<ExchangeCurrency> ec =exchangeCurrencyRepo.findBySourceCurrencyAndTargetCurrency(sourceCurrency,targetCurrency);
			if (!ec.isPresent()) {
				return new ResponseEntity<>("no exchnage rate exists try to add new exchange rate", HttpStatus.NOT_FOUND);
			} else {
				ec.get().setExchangeRate(rate);
				ExchangeCurrency t= exchangeCurrencyRepo.save(ec.get());
				return new ResponseEntity<>(t, HttpStatus.OK);
			}
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getMessage());
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	

	
}