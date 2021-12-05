package com.coffepotato.config;

import com.coffepotato.common.component.JWTCore;
import com.coffepotato.common.filter.ServletAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecureConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JWTCore jwtCore;


    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/*8");
        //static 디렉터리의 하위 파일 목록은 인증 무시 (통과)
    }



    @Override
    public void configure(HttpSecurity http)throws  Exception{
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new ServletAuthFilter(jwtCore) ,UsernamePasswordAuthenticationFilter.class);



    }
}
