package com.coffepotato.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class AuthServerErrorControler implements ErrorController {

    private static final String COMMON_PATH = "/error";


    @RequestMapping("/error")
    public ModelAndView errorHandle(HttpServletRequest request , HttpServletResponse response){

        Object status  = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView modelAndView = new ModelAndView();

        log.error(String.valueOf(response.getStatus()));

        if(status !=null){
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                modelAndView.setViewName(COMMON_PATH + "/404");
            }else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                modelAndView.setViewName(COMMON_PATH +"/500");
            }else if (statusCode == HttpStatus.FORBIDDEN.value()){
                modelAndView.setViewName(COMMON_PATH + "/403");
            }else{
                modelAndView.setViewName(COMMON_PATH + "/common");
            }

        }
        return  modelAndView;
    }
}
