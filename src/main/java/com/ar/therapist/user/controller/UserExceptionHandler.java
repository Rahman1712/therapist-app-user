package com.ar.therapist.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ar.therapist.user.exception.UserException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserException.class)
	public ResponseEntity<?> handleUserFails(UserException ex){
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", ex.getMessage());
		log.error("UserException : {}", ex.getMessage());
		return ResponseEntity.badRequest().body(errorMap);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserRegisterException.class)
	public ResponseEntity<?> handleRegisterFails(UserRegisterException ex){
		return ResponseEntity.badRequest().body(ex.getErrorResponse());
	}
	
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        // You can customize the error response message here
        String errorMessage = "Username not found: " + ex.getMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> handleRegisterFails(ExpiredJwtException ex){
		//return ResponseEntity.badRequest().body(ex.getMessage());
		return ResponseEntity.badRequest().body("Expired : Time Limit Exceeded ⌛");
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<?> handleValidationExceptions(MethodArgumentNotValidException ex){
		List<Map<String, String>> list = new ArrayList<>();
		System.err.println(ex.getBindingResult().getAllErrors());
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			Map<String, String> errors = new HashMap<>();
			errors.put("fieldName", fieldName);
			errors.put("errorMessage", errorMessage);
			list.add(errors);
		});
		return list; 
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(MalformedJwtException.class)
	public Map<String, String> handleJwtException(MalformedJwtException ex){
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", ex.getMessage());
		return errorMap;
	}
	
//	@ExceptionHandler(UserException.class)
//	public ResponseEntity<ErrorDetail> UserExceptionHandler(UserException e, WebRequest req){
//		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
//		
//		return new ResponseEntity<ErrorDetail>(err, HttpStatus.BAD_REQUEST);
//	}
}