package com.coffepotato.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwkSetRestControler {


    private final JWKSet jwkSet;

    @Autowired
    public JwkSetRestControler(JWKSet jwkSet) {
        this.jwkSet = jwkSet;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String,Object> Pubkeys(){
        return jwkSet.toJSONObject();
    }
}
