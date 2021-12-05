package com.coffepotato;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("템플릿 엔진 테스트")
//@ExtendWith(SpringExtension.class)
public class MustacheTemplateEngineTests {

    //@Autowired
    private TestRestTemplate restTemplate;

    //@Test
    @DisplayName("메인페이지 로딩")
    public void MainLoadTest(){

        String body = this.restTemplate.getForObject("/",String.class);
        

    }
    
    //@Test
    public void ErrorPageLoadingTest(){
        System.out.println("커밋테스트");
    }
}
