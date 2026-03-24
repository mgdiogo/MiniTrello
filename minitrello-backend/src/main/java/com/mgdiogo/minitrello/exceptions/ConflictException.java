package com.mgdiogo.minitrello.exceptions;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
	private final String field;

	public ConflictException (String msg, String field) {
		super(msg);
		this.field = field;
	}
}
