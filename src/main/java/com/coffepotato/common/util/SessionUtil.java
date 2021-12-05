package com.coffepotato.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SessionUtil {

    public  static String  getHeaderInfo(String subject) {
        //현재 request의 Header 정보를 읽어옴.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        return request.getHeader(subject) == null ? "" : request.getHeader(subject) ;
    }
}
