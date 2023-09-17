package com.cepa.generalservice.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public String getSubfixApi(String url){
        String result = "";
        String[] apiList = url.split("/");
        result = apiList[3];
        return result;
    }
}
