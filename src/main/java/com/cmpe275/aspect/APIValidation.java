package com.cmpe275.aspect;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.Exception.ValidationException;
import com.cmpe275.entity.Offer;

@Aspect
@Order(1)
@Component
public class APIValidation {
	
//    checking the required parameters for createPlayer
	
//	public void ValidateCreatePlayerAPI(JoinPoint joinPoint, HttpServletRequest request) {
//		if (request.getParameter("username") == null || request.getParameter("username") == "") {
//			throw new ValidationException("Email is missing which is required parameter.");
//		}
//		if (request.getParameter("nickname") == null || request.getParameter("nickname") == "") {
//			throw new ValidationException("nickname is missing which is required parameter.");
//		}
//		
//	}
//	
	
	@Before("execution(public * com.cmpe275.controller.ExchangeRateController.createOffer(..))")
	public void ValidateExchangeOffer(JoinPoint joinPoint) throws CustomException {
	   
		
		Object[] shareSecret = (Object[])joinPoint.getArgs();
		Offer offer = (Offer) shareSecret[0];
		
		if (offer.getAmount() == 0) {
			throw new ValidationException("Amount is missing which is the required parameter");
		}
		if (offer.getSourceCountry() == null ) {
			throw new ValidationException("Source Country is missing which is the required parameter");
		}
		if (offer.getSourceCurrency() == null) {
			throw new ValidationException("Source Currency is missing which is the required parameter");
		}
		if (offer.getDestinationCountry() == null) {
			throw new ValidationException("Destination Country is missing which is the required parameter");
		}
		if (offer.getDestinationCurrency() == null) {
			throw new ValidationException("Destination Currency is missing which is the required parameter");
		}
		if (offer.getExpiry() == null) {
			throw new ValidationException("Expiry Date is missing which is the required parameter");
		}

	}


}