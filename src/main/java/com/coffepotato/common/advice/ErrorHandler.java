package com.coffepotato.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;

@ControllerAdvice
@Slf4j
public class ErrorHandler {


    @ExceptionHandler(ServletException.class)
    protected void handleServeletException(){

    }
}
