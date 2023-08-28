package com.cepa.generalservice.exceptions.message;

import org.springframework.stereotype.Component;

@Component
public class Message {
    public String objectExistMessage(String objectName, String value) {
        return objectName + " name " + value + " already exist.";
    }

    public String objectNotFoundByIdMessage(String objectName, Object value) {
        return objectName + " id " + value + " not found.";
    }

    public String emptyList(String listName){
        return "Cannot found any " + listName +" in database";
    }
    public String badValue(String objectName) {
        return "The " + objectName + " not valid";
    }
    public String invalidUser(){
        return "Login to perform this function";
    }

}
