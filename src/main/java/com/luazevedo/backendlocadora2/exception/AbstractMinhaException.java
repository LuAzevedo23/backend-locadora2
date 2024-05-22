package com.luazevedo.backendlocadora2.exception;

public abstract class AbstractMinhaException extends RuntimeException {
    public AbstractMinhaException(String message) {
        super(message);
    }

    public AbstractMinhaException(String message, Throwable cause) {
        super(message, cause);
    }
}



