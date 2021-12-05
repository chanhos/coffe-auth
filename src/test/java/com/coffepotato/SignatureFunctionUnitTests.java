package com.coffepotato;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.coffepotato.common.util.ECcryptor;
import org.apache.tomcat.util.codec.binary.Base64;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;


@DisplayName("서명및 암호화 관련 컴포넌트 단위 테스트")
@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes =  CoffeAuthApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SignatureFunctionUnitTests {



    @DisplayName("JJWT 토큰 생성 테스트")
    @Order(1)
    @Test
    void jjwtGenerateECKey() {
            KeyPair keypair = Keys.keyPairFor(SignatureAlgorithm.ES256);
            System.out.println("EC privatKey :" + Base64.encodeBase64String( keypair.getPrivate().getEncoded()));
            System.out.println("EC publicKey :" + Base64.encodeBase64String( keypair.getPublic().getEncoded()));

            String jwt = Jwts.builder()
                    .setSubject("Clark")
                    .signWith(keypair.getPrivate()).compact();

            System.out.println(jwt);
            Jws<Claims> jws ;
            jws = Jwts.parserBuilder().setSigningKey(keypair.getPublic()).build().parseClaimsJws(jwt);

            String expectSub =  jws.getBody().get("sub",String.class);

            assertEquals("Clark",expectSub);

    }

    @DisplayName("Base64인코딩 테스트")
    @Test
    public void Base64Encoding() throws JsonProcessingException {

        final ObjectMapper objectMapper = new ObjectMapper();

        final Map<String,Object> header = new LinkedHashMap<>();
        header.put("typ" , "JWT");
        header.put("alg" , "EC256");
        byte[] byteHader ;
        try{
            byteHader = objectMapper.writeValueAsBytes(header);
        }catch (JsonProcessingException ex){
            byteHader = null;
        }

        assert byteHader != null;

        String encodedHeader = Base64Utils.encodeToUrlSafeString(byteHader);

        System.out.println(encodedHeader);


        byte[] decodedHeader =  Base64.decodeBase64URLSafe(encodedHeader);

        String decodeStr = new String(decodedHeader);

        Gson gson = new Gson();

        Object obj =  gson.fromJson(decodeStr, Object.class );

        System.out.println(obj.toString());


    }



    @DisplayName("PEM키파일 생성 후 로드하여 JWT사인")
    @Test
    public  void genAndSignJWT(){
        ECcryptor ec = new ECcryptor();
        try{
            ec.generate("private.pem","public.pem");
            PrivateKey privateKey = ec.readPrivatKeyFromPEMFile("private.pem");
            PublicKey publicKey = ec.readPublicKeyFromPemFile("public2.pem");

            String jwt = Jwts.builder()
                    .setSubject("Clark")
                    .signWith(privateKey).compact();

            System.out.printf(String.format("JWT TOKEN ISSUED : %s \n",jwt));

            assertNotEquals("",jwt);

            Jws<Claims> jws ;
            jws = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwt);
            System.out.printf(String.format("JWT TOKEN Decoded : %s \n",jws.toString()));


        }catch (Exception ex){
            System.out.println("오류발생.");
            System.out.println(ex.getMessage());
        }

    }

    @DisplayName("JWT서명 검증테스트")
    @Test
    public void JWTauthenticSignTest() {

        try{
            ECcryptor ec = new ECcryptor();
            String basePath = "./.pem".replaceAll("/", Matcher.quoteReplacement(File.separator));
            String privatePath = basePath + File.separator + "sign_publickey.pem";
            PublicKey publicKey = ec.readPublicKeyFromPemFile(privatePath);

            String jwt =  "eyJ0eXAiOiJKV1RTIiwiYWxnIjoiRVMyNTYifQ.eyJpc3MiOiJodHRwOi8vbHVuYWdsb2JhbGF1dGguY28ua3IiLCJzdWIiOiJMMjEwMzAyIiwiaWF0IjoxNjM1NzU0MTg5LCJleHAiOjE2MzU3NTU5ODl9.ZeY8N7_M_nNd6UK5ZIWcv2U6a8Eub9RGN6F0WF_bNY_4qYsc-FvJTRK1zLjTLhe20QUNoBYcQt2MRnru9C-hXQ";

            Jws<Claims> jws ;
            jws = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwt);

            System.out.printf(String.format("JWT TOKEN Decoded : %s \n",jws.toString()));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }


    }


    //@DisplayName("KeyStore 파일 로딩 테스트")
    //@Test
    public void JKSFileLoadingAndMakeKeyPairTest(){

        try{
            ClassPathResource jwkFile = new ClassPathResource("coffe.jks");

            Path path = Paths.get(jwkFile.getURI());
            //List<String> content = Files.readAllLines(path);
            //content.forEach(System.out::println);
            System.out.println(path);


            KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(jwkFile,"12234!".toCharArray());

            KeyPair keyPair = keyFactory.getKeyPair("12234!");

            assertNotNull(keyPair);

            System.out.println(keyPair.getPublic());
            System.out.println(keyPair.getPrivate());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }




}
