package com.cepa.generalservice.event;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class EventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> data = new LinkedHashMap();
        data.put("URI", request.getRequestURI());
        data.put("host",request.getHeader("host"));
        data.put("user-agent",request.getHeader("User-Agent"));
        data.put("server", request.getServerName());
        applicationEventPublisher.publishEvent(new Event(this,data));
    }
}
