package com.tyremandi.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.tyremandi.ui.response.model.ErrorMessage;



@ControllerAdvice
public class AppExceptionsHandler {
	@ExceptionHandler(value = {UserServiceException.class})
	public ResponseEntity<Object> handlerUserServiceException(UserServiceException ex , WebRequest request ){
		
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), new Date());
		
		return new ResponseEntity(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}
	
	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handlerAnyOtherException(Exception ex , WebRequest request ){
		
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), new Date());
		
		return new ResponseEntity(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR );
		
	}

}
