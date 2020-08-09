package com.interswitch.notification.core.exceptions;

public class AppBaseException extends RuntimeException {

    public AppBaseException() {
        super("Failed to perform the requested action");
    }

    public AppBaseException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public AppBaseException(String message) {
        super(message);
    }

    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
