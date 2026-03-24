package com.mgdiogo.minitrello.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
	private String timestamp;
	private int status;
	private String error;
	private String message;
	private String field;
}
