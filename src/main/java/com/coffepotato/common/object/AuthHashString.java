package com.coffepotato.common.object;

public interface AuthHashString {

    String getHashMethod();

    boolean authenticateString(String plainText , String hashedString);
}
