package com.coffepotato.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.coffepotato.common.component.JWTCore;
import com.coffepotato.common.component.JWTSecureManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SignSecureConfig {

   /*
    @Bean(name = "jwtSecureManager")
    public JWTSecureManager setJWTSecureManager() throws Exception {
        //String basePath = sourcePemPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
        //String privatePath = basePath + File.separator + privatePem;
        //String publicPath = basePath + File.separator + publicPem;
        //JWTSecureManager jwtSecureManager = new JWTSecureManager(privatePath,publicPath);
        //jwtSecureManager.init();
        return jwtSecureManager;
    }
    */

    @Bean
    public JWTCore setJWTCore(@Qualifier("JWTSecureManager")JWTSecureManager jwtSecureManager){
       return  new JWTCore(jwtSecureManager);
    }

    @Bean
    public JWKSet jwkSet(@Qualifier("JWTSecureManager") JWTSecureManager jwtSecureManager){
         return jwtSecureManager.setJWKSet();
    }

}
