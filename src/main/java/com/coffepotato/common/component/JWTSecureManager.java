package com.coffepotato.common.component;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.coffepotato.common.util.ECcryptor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author Chan's
 * <p>
 * Description : JWT 토큰의 서명을 진행하고 관리하는 매니징 클래스 <br>
 * </p>
 * <br>
 * Updated :   2021/11/17
 */
@DependsOn({"S3Bucket"})
@Getter
@Setter
@Component("JWTSecureManager")
@Slf4j
public class JWTSecureManager {

    @Value("${config.jwtsign.source-pem-path}")
    private String sourcePemPath;

    @Value("${config.jwtsign.private-pem}")
    private String private_key_path;

    @Value("${config.jwtsign.public-pem}")
    private String public_key_path;

    //S3 모듈
    @Resource(name = "S3Client")
    private S3Client s3Client;
    @Resource(name = "S3Bucket")
    private Map<String,String> bucket;

    //서명 암호화 (ES256) 를 위한 비대칭키 암호화 모듈
    private ECcryptor eCcryptor = new ECcryptor();

    //서버 소유 서명 개인키
    PrivateKey server_privateKey;

    //서버 소유 서명 공개키
    PublicKey server_publicKey;

    /**
     * 초기화 작업 :  서명을 위한 개인키/공개기 를 로드한다.
     * @throws Exception
     */
    @PostConstruct
    public void  init() throws Exception {

        String basePath = sourcePemPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
        private_key_path = basePath + File.separator + private_key_path;
        public_key_path = basePath + File.separator + public_key_path;

        //두개 PEM 파일중 어느 하나라도 존재 하지 않는경우./
        if(!(new File(private_key_path).exists()) || !(new File(public_key_path).exists()) ){
            eCcryptor.generate(private_key_path,public_key_path);
            //generateByS3();
        }
        server_privateKey = eCcryptor.readPrivatKeyFromPEMFile(private_key_path);
        server_publicKey = eCcryptor.readPublicKeyFromPemFile(public_key_path);
    }

    /**
     * 초기화 작업 : 서명읠 위한 개인키/공개키를 s3자원을 통해 로드한다.
     */
    private void generateByS3(){
        String bucket_name  = bucket.get("keystore");
        String bucket_private_key  = "keystore/private_key.pem";
        String bucket_public_key  = "keystore/public_key.pem";

        try{
            s3Client.downLoadObject(bucket_name , bucket_private_key, getPrivate_key_path());
            s3Client.downLoadObject(bucket_name , bucket_public_key , getPublic_key_path());
            log.info("s3 객체를 통해 알고리즘 키 한쌍을 생성하엿습니다.");
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

    /**
     * 설정된 공개키를  JsonWebKey 형식으로 API 상에 노출 시킨다.
     * @return
     */
    public JWKSet setJWKSet(){
        ECKey.Builder builder = new ECKey.Builder(Curve.P_256,(ECPublicKey) server_publicKey)
                                            .keyUse(KeyUse.SIGNATURE).algorithm(JWSAlgorithm.ES256).keyID("coffe-auth");

        return  new JWKSet(builder.build());
    }
}
