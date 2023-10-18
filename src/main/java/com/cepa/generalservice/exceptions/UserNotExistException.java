package com.cepa.generalservice.exceptions;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException() {}

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
