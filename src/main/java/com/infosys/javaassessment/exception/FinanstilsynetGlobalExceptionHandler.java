package com.infosys.javaassessment.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FinanstilsynetGlobalExceptionHandler extends ResponseEntityExceptionHandler{

	/*
	 * Method to handle InstrumentNotFoundException
	 */
	@ExceptionHandler(InstrumentNotFoundException.class)
	public final ResponseEntity<Object> handleInstrumentNotFoundException(InstrumentNotFoundException ex){
		ExceptionResponse exceptionResponse = ExceptionResponse.of(new Date(), "InstrumentNotFoundException",
				ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/*
	 * Method to handle InvalidInputException
	 */
	@ExceptionHandler(InvalidInputException.class)
	public final ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {
		ExceptionResponse exceptionResponse = ExceptionResponse.of(new Date(), "InvalidInputException",
				ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
}
