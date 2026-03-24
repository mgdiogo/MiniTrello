package com.mgdiogo.minitrello.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String field;

    public ResourceNotFoundException(String msg, String field) {
        super(msg);
        this.field = field;
    }
}
