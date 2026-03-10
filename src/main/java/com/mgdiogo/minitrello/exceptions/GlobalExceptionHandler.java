package com.mgdiogo.minitrello.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	// Throws custom unauthorized error
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorTemplate> handleBadCredentials(BadCredentialsException badCredentials) {
		ErrorTemplate request = new ErrorTemplate(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
				"Invalid email or password");

		return new ResponseEntity<>(request, HttpStatus.UNAUTHORIZED);
	}

	// Catches any unhandled exceptions and returns a generic error response
	@ExceptionHandler(Exception.class) // catch-all — always have this
	public ResponseEntity<ErrorTemplate> handleUnexpected(Exception ex) {
		log.error("Unexpected error", ex); // log the real cause server-side
		ErrorTemplate error = new ErrorTemplate(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "An unexpected error occurred. Please try again later.");
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
