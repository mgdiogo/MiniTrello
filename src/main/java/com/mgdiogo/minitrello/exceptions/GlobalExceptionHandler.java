package com.mgdiogo.minitrello.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mgdiogo.minitrello.dtos.responses.ErrorResponse;

import org.springframework.security.authentication.BadCredentialsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	// Throws custom errors for DTO validaton rules
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		FieldError fieldError = ex.getBindingResult().getFieldError();

		String message = fieldError != null ? fieldError.getDefaultMessage() : "Validation check failed";
		ErrorResponse error = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.BAD_REQUEST.value(), "Bad Request",
				message);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// Throws custom bad request error
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException badRequest) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.BAD_REQUEST.value(), "Bad Request",
				badRequest.getMessage());

		return new ResponseEntity<>(request, HttpStatus.BAD_REQUEST);
	}

	// Throws custom conflict error
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflictException(ConflictException conflictException) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.CONFLICT.value(), "Conflict",
				conflictException.getMessage());

		return new ResponseEntity<>(request, HttpStatus.CONFLICT);
	}

	// Throws custom unauthorized error
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException badCredentials) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
				"Invalid email or password");

		return new ResponseEntity<>(request, HttpStatus.UNAUTHORIZED);
	}

	// Catches any unhandled exceptions and returns a generic error response
	@ExceptionHandler(Exception.class) // catch-all — always have this
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
		log.error("Unexpected error", ex); // log the real cause server-side
		ErrorResponse error = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "An unexpected error occurred. Please try again later.");
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
