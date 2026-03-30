package com.mgdiogo.minitrello.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
	private final String field;

	public BadRequestException(String msg, String field) {
		super(msg);
		this.field = field;
	}
}
