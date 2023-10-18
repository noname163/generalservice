package com.cepa.generalservice.exceptions;

public class InValidInformation extends RuntimeException{
    public InValidInformation() {}

    public InValidInformation(String message) {
        super(message);
    }

    public InValidInformation(String message, Throwable cause) {
        super(message, cause);
    }
}
