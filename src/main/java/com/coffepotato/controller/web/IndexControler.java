package com.coffepotato.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControler {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/manager_admin/main")
    public  String toManager(){
        return "manager";
    }

    //에러페이지 맵핑
    @GetMapping("/error")
    public  String toError(){
        return "ERR";
    }

    //에러페이지 맵핑
    @GetMapping("/error/500")
    public  String toError500(){
        return "ERR500";
    }

    //에러페이지 맵핑
    @GetMapping("/error/404")
    public  String toError404(){
        return "ERR404";
    }

    //에러페이지 맵핑
    @GetMapping("/error/403")
    public  String toError403(){
        return "ERR403";
    }

    //에러페이지 맵핑
    @GetMapping("/error/common")
    public  String toErrorCommon(){
        return "COMMON";
    }
}
