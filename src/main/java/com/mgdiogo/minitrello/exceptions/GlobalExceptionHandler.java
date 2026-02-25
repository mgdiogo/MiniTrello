package com.mgdiogo.minitrello.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Throws custom errors for DTO validaton rules
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorTemplate> handleValidation(MethodArgumentNotValidException ex) {
		FieldError fieldError = ex.getBindingResult().getFieldError();

		String message = fieldError != null ? fieldError.getDefaultMessage() : "Validation check failed";
		ErrorTemplate error = new ErrorTemplate(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
				message);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// Throws custom bad request error
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorTemplate> handleBadRequest(BadRequestException badRequest) {
		ErrorTemplate request = new ErrorTemplate(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
				badRequest.getMessage());

		return new ResponseEntity<>(request, HttpStatus.BAD_REQUEST);
	}

	// Throws custom conflict error
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorTemplate> handleConflictException(ConflictException conflictException) {
		ErrorTemplate request = new ErrorTemplate(LocalDateTime.now(), HttpStatus.CONFLICT.value(), "Conflict",
				conflictException.getMessage());

		return new ResponseEntity<>(request, HttpStatus.CONFLICT);
	}

}
