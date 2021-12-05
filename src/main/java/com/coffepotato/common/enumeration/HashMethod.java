package com.coffepotato.common.enumeration;

public enum HashMethod {
    SHA256("SHA256"),
    SHA1("SHA1"),
    MD5("MD5");

    HashMethod(String value) { this.value =value;}

    private final String value;

    public String value(){return  value;}
}
