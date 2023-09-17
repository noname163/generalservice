package com.cepa.generalservice.event;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class Event extends ApplicationEvent {

    private Map data;

    public Event(Object source, Map data) {
        super(source);
        this.data = data;
    }
    
}
