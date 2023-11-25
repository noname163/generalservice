package com.cepa.generalservice.utils;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.object.MediaType;

@Component
public class StringUtil {
    public String getSubfixApi(String url){
        String result = "";
        String[] apiList = url.split("/");
        result = apiList[3];
        return result;
    }

    public MediaType convertStringToMediaType(String mediaTypeString) {
        String[] split = mediaTypeString.split("/");
        return MediaType
                .builder()
                .mediaType(split[0])
                .subfix(split[1])
                .build();
    }
}
