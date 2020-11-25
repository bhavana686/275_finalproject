
package com.cmpe275.Exception;

import org.springframework.http.HttpStatus;


public class CustomException extends Exception {
	/**
	 Exceptions to handle Error Code
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpStatus errorCode;

	public CustomException(String message, HttpStatus errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public HttpStatus getErrorCode() {
		return this.errorCode;
	}
}
