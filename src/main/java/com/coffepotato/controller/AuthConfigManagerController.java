package com.coffepotato.controller;

import com.coffepotato.services.UserManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.coffepotato.services.ProvidedServicesService;
import com.coffepotato.services.RefreshTokenManageService;
import com.coffepotato.vo.common.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@Tag(name = "Coffe Auth API - Admin Managing" , description = "[관리자 API]")
@CrossOrigin(origins = "*")
@RequestMapping(value = "/manage")
@RestController
public class AuthConfigManagerController {

    @Autowired
    private ProvidedServicesService providedServicesService;

    @Autowired
    private RefreshTokenManageService refreshTokenManageService;

    @Autowired
    private UserManageService userManageService;


    @Operation(description = "인증서버 연결 서비스조회", summary = "인증서버 연결 서비스조회" )
    @RequestMapping(value = "/provided_services",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseVo> getProividedServices(){

        return  new ResponseEntity<>(providedServicesService.getProvidedServicesAll(), HttpStatus.OK);
    }


    @Operation(description = "리프레쉬 토큰 조회" ,summary =  "리프레쉬 토큰 조회")
    @RequestMapping(value = "/refreshtoken/list",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseVo> getRefreshTokenList(){

        return  new ResponseEntity<>(refreshTokenManageService.getRefreshTokenList(), HttpStatus.OK);
    }

    @Operation(description = "사용자 조회" , summary = "사용자 조회")
    @RequestMapping(value = "/users/list/{member_id}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseVo> getUserList(@PathVariable(value = "member_id") String member_id)
    {
        return  new ResponseEntity<>(userManageService.getUserList(member_id), HttpStatus.OK);
    }
}
