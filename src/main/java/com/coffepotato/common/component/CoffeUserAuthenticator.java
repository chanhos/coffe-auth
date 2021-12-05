package com.coffepotato.common.component;

import com.google.common.hash.Hashing;
import com.coffepotato.common.enumeration.HashMethod;
import com.coffepotato.common.object.AuthHashString;

import java.nio.charset.StandardCharsets;

/**
 * @author Chan's
 * <p>
 * Description : 사용자의 Pawwsord 인증을 위한 모듈  <br>
 * </p>
 * <br>
 * Updated : 2021/11/17
 */
public class CoffeUserAuthenticator implements AuthHashString {

    private HashMethod hashMethod;

    public CoffeUserAuthenticator(String methodChar){
        switch (methodChar){
            case "MD5" :
                hashMethod = HashMethod.MD5;
                break;
            case "SHA1":
                hashMethod = HashMethod.SHA1;
                break;
            case "SHA256":
            default:
                hashMethod = HashMethod.SHA256;
                break;

        }
    }

    @Override
    public String getHashMethod() {
        return hashMethod.value();
    }

    /**
     * 사용자의 Password 인증을 진행한다.
     * @param plainText      비밀번호 평문 (입력값)
     * @param hashedString   비밀번호 Hash값(대조값)
     * @return               사용자 인증여부
     */
    @Override
    public boolean authenticateString(String plainText, String hashedString) {
        try{
            String hashedPlainText = "";
            switch (hashMethod){
                case MD5:
                    hashedPlainText = Hashing.md5().hashString(plainText,StandardCharsets.UTF_8).toString();
                    break;
                case SHA1:
                    hashedPlainText = Hashing.sha1().hashString(plainText,StandardCharsets.UTF_8).toString();
                    break;
                case SHA256:
                default:
                    hashedPlainText = Hashing.sha256().hashString(plainText, StandardCharsets.UTF_8).toString();
            }
            return (hashedPlainText.equals(hashedString));
        }catch (Exception ex){
            return false;
        }
    }
}
