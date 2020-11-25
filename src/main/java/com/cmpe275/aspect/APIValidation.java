package com.cmpe275.aspect;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.cmpe275.Exception.ValidationException;

@Aspect
@Order(1)
@Component
public class APIValidation {
	
//    checking the required parameters for createPlayer
	@Before("execution(public * com.cmpe275.controller.UserController.userSignup(..)) && args(request)")
	public void ValidateCreatePlayerAPI(JoinPoint joinPoint, HttpServletRequest request) {
		if (request.getParameter("username") == null || request.getParameter("username") == "") {
			throw new ValidationException("Email is missing which is required parameter.");
		}
		if (request.getParameter("nickname") == null || request.getParameter("nickname") == "") {
			throw new ValidationException("nickname is missing which is required parameter.");
		}
		
	}


}