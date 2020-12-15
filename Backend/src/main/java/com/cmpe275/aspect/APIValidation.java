package com.cmpe275.aspect;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.Exception.ValidationException;
import com.cmpe275.entity.Offer;
import com.fasterxml.jackson.databind.JsonNode;
import com.cmpe275.entity.Enum;

@Aspect
@Order(1)
@Component
public class APIValidation {
	
	@Before("execution(public * com.cmpe275.controller.ExchangeRateController.createOffer(..))")
	public void ValidateExchangeOffer(JoinPoint joinPoint ) {
		JsonNode body = (JsonNode) joinPoint.getArgs()[1];
		if (body.get("destinationCountry") == null || body.get("destinationCountry").asText() == "") {
			throw new ValidationException("Destination Country should not be null");
		}
		if (body.get("destinationCurrency") == null || body.get("destinationCurrency").asText() == "") {
			throw new ValidationException("Destination Currency should not be null");
		}
		if (body.get("sourceCountry") == null || body.get("sourceCountry").asText() == "") {
			throw new ValidationException("Source Country should not be null");
		}
		if (body.get("sourceCurrency") == null || body.get("sourceCurrency").asText() == "") {
			throw new ValidationException("Source Currency should not be null");
		}
		if (body.get("amount") == null || body.get("amount").asText() == "") {
			throw new ValidationException("amount should not be null");
		}
		if (body.get("expiry") == null || body.get("expiry").asText() == "") {
			throw new ValidationException("expiry should not be null");
		}
		
	}

	@Before("execution(public * com.cmpe275.controller.ExchangeRateController.updateOffer(..))")
	public void ValidateExchangeUpdateOffer(JoinPoint joinPoint ) {

		System.out.println("hii"+ (Long) joinPoint.getArgs()[1]);
		System.out.println("hii"+ (JsonNode) joinPoint.getArgs()[0]);
		JsonNode body = (JsonNode) joinPoint.getArgs()[0];
		if (body.get("destinationCountry") == null || body.get("destinationCountry").asText() == "") {
			throw new ValidationException("Destination Country should not be null");
		}
		if (body.get("destinationCurrency") == null || body.get("destinationCurrency").asText() == "") {
			throw new ValidationException("Destination Currency should not be null");
		}
		if (body.get("sourceCountry") == null || body.get("sourceCountry").asText() == "") {
			throw new ValidationException("Source Country should not be null");
		}
		if (body.get("sourceCurrency") == null || body.get("sourceCurrency").asText() == "") {
			throw new ValidationException("Source Currency should not be null");
		}
		if (body.get("amount") == null || body.get("amount").asText() == "") {
			throw new ValidationException("amount should not be null");
		}
		if (body.get("expiry") == null || body.get("expiry").asText() == "") {
			throw new ValidationException("expiry should not be null");
		}
		
		
		
	}
	@Before("execution(public * com.cmpe275.controller.UserController.userSignup(..))")
	public void ValidateUserSignup(JoinPoint joinPoint ) {
		
		JsonNode body = (JsonNode) joinPoint.getArgs()[1];
		System.out.print(body);
		if (body.get("username") == null || body.get("username").asText() == "") {
			throw new ValidationException(" username should not be null");
		}
		if (body.get("nickname") == null || body.get("nickname").asText() == "") {
			throw new ValidationException("nickname should not be null");
		}
		if (body.get("password") == null || body.get("password").asText() == "") {
			throw new ValidationException(" password should not be null");
		}
		if (body.get("signupType") == null || body.get("signupType").asText() == "") {
			throw new ValidationException(" signupType should not be null");
		}
		if (body.get("isVerified") == null) {
			throw new ValidationException("isVerified should not be null");
		}
	}

	@Before("execution(public * com.cmpe275.controller.BankAccountController.addAccount(..))")
	public void ValidateAddBankAccount(JoinPoint joinPoint ) {
	    
		if((Enum.Currency) joinPoint.getArgs()[2]==null) {
			throw new ValidationException(" currency should not be null");
		}
		if((Enum.Countries) joinPoint.getArgs()[3]==null) {
			throw new ValidationException(" country should not be null");
		}
		
	}

}