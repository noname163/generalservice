package com.cepa.generalservice.exceptions;

public class DataConfilictException extends RuntimeException {
    public DataConfilictException() {}

    public DataConfilictException(String message) {
        super(message);
    }

    public DataConfilictException(String message, Throwable cause) {
        super(message, cause);
    }
}
