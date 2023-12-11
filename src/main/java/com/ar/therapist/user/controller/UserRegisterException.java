package com.ar.therapist.user.controller;

import com.ar.therapist.user.exception.ErrorResponse;

public class UserRegisterException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ErrorResponse errorResponse;

	public UserRegisterException(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
}
