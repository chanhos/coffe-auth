package com.coffepotato.services;

import com.coffepotato.vo.common.UserSignInVO;
import com.coffepotato.vo.common.ResponseVo;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserSignInService {

    public ResponseEntity<ResponseVo> SignInCoffeUser(UserSignInVO signIn);

    public Map<String,Object> createAllToken(String auth_key) throws Exception;


    public ResponseEntity<ResponseVo> reIsuueAccessKey();
}
