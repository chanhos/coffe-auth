package com.coffepotato.common.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Chan's
 * <p>
 * Description : ES256 암호화 알고리즘으로 개인키/공개키를 PEM파일로 생성하고 로드하는 매니징 클래스 A<br>
 * </p>
 * <br>
 * Updated :
 */
@Slf4j
public class ECcryptor {


    public void generateByKeyStore() throws Exception {
        /*
        ClassPathResource ksFile = new ClassPathResource("jwtSeed.jks");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(ksFile.getInputStream() , "coffeboy".toCharArray());

        keyStore.getKey("coffe-auth","coffeboy".toCharArray()).

         */

    }



    public void generate(String privateKeyFileName , String publicKeyFileName){
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);

        PrivateKey priv = keyPair.getPrivate();
        PublicKey pub = keyPair.getPublic();
        try{
            writePemFile(priv,"PRIVATE KEY", privateKeyFileName);
            writePemFile(pub,"PUBLIC KEY", publicKeyFileName);
            log.info("타원곡선 암호화 알고리즘 키 한쌍을 생성했습니다. ");
        }catch (Exception ex){
            log.error("키 생성 도중 오류가 발생하였습니다.");
        }

    }

    private void writePemFile(Key key, String description, String filename) throws IOException {
        PEMGenerator.build(key,description).write(filename);
        log.info(String.format("%s을(를) %s 파일에 저장하였습니다.", description, filename));
    }

    public PrivateKey readPrivatKeyFromPEMFile(String privateKeyName) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        String data = readString(privateKeyName);
        log.info("ES256 개인키를" +privateKeyName + "로부터 불러왔습니다." );
        //System.out.printf(data);

        data = data.replaceAll("-----BEGIN PRIVATE KEY-----", "");
        data = data.replaceAll("-----END PRIVATE KEY-----", "");
        byte[] decoded = Base64.decode(data);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        KeyFactory factory = KeyFactory.getInstance("EC");

        return factory.generatePrivate(spec);
    }

    public PublicKey readPublicKeyFromPemFile(String publickeyName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String data = readString(publickeyName);
        log.info("ES256 공개키를" +publickeyName + "로부터 불러왔습니다." );
        //System.out.printf(data);

        data = data.replaceAll("-----BEGIN PUBLIC KEY-----" , "");
        data = data.replaceAll("-----END PUBLIC KEY-----" , "");

        byte[] decoded = Base64.decode(data);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory  factory = KeyFactory.getInstance("EC");
        return factory.generatePublic(spec);
    }

    private String readString(String filename) throws FileNotFoundException ,IOException{
        StringBuilder pem = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while( (line = br.readLine()) != null){
            pem.append(line).append('\n');
        }
        br.close();
        return pem.toString();
    }



}
