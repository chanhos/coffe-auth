package com.coffepotato.common.enumeration;

public enum Errorcode {
    NOERROR(0),
    RUNTIME(1),
    LOGICAL_THROW_ERROR(2);


    Errorcode(int value) { this.value = value;}
    private final int value;
    public int value(){ return  value;}
}
