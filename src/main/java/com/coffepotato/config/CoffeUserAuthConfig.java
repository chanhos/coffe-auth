package com.coffepotato.config;

import com.coffepotato.common.component.CoffeUserAuthenticator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "config.auth")
public class CoffeUserAuthConfig {

    private String hashMethod;

    @Bean
    public CoffeUserAuthenticator setAuthenticateSignInMethod(){
        return new CoffeUserAuthenticator(hashMethod);
    }
}
