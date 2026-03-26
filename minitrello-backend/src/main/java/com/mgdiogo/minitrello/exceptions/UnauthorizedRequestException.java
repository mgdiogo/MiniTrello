package com.mgdiogo.minitrello.exceptions;

import lombok.Getter;

@Getter
public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException(String msg) {
        super(msg);
    }
}
