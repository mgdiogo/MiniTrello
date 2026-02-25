package com.mgdiogo.minitrello.exceptions;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ErrorTemplate {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;
}
