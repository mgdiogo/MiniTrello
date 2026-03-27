package com.mgdiogo.minitrello.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

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
		String field = fieldError != null ? fieldError.getField() : null;
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.BAD_REQUEST.value(), "Bad Request",
				message, field);
		return new ResponseEntity<>(request, HttpStatus.BAD_REQUEST);
	}

	// Throws error when an endpoint is not found
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER),HttpStatus.NOT_FOUND.value(),"Not Found",
			"Endpoint not found", null);
		return new ResponseEntity<>(request, HttpStatus.NOT_FOUND);
	}

	// Throws error when data inside a valid endpoint is not found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException notFound) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.NOT_FOUND.value(), "Not Found",
				notFound.getMessage(), notFound.getField());
		return new ResponseEntity<>(request, HttpStatus.NOT_FOUND);
	}

	// Throws custom bad request error
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException badRequest) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.BAD_REQUEST.value(), "Bad Request",
				badRequest.getMessage(), badRequest.getField());

		return new ResponseEntity<>(request, HttpStatus.BAD_REQUEST);
	}

	// Throws custom conflict error
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflictException(ConflictException conflictException) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.CONFLICT.value(), "Conflict",
				conflictException.getMessage(), conflictException.getField());

		return new ResponseEntity<>(request, HttpStatus.CONFLICT);
	}

	// Throws custom wrong credentials error
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException badCredentials) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
				"Invalid email or password", "email");

		return new ResponseEntity<>(request, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UnauthorizedRequestException.class)
	public ResponseEntity<ErrorResponse> handleUnathorizedRequest(UnauthorizedRequestException unauthorizedRequest) {
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
				unauthorizedRequest.getMessage(), null);

		return new ResponseEntity<>(request, HttpStatus.UNAUTHORIZED);
	}

	// Catches any unhandled exceptions and returns a generic error response
	@ExceptionHandler(Exception.class) // catch-all — always have this
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
		log.error("Unexpected error", ex); // log the real cause server-side
		ErrorResponse request = new ErrorResponse(LocalDateTime.now().format(FORMATTER), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "An unexpected error occurred. Please try again later.", null);
		return new ResponseEntity<>(request, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
