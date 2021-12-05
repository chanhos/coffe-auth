package com.coffepotato.common.util;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public class ModelWrapper {

    private static ModelMapper mapper = new ModelMapper();

    public static void  Map(Object source, Object destination){
        mapper.map(source,destination);
    }

    public static <T> T Mapping(Object source, T destination){
        mapper.map(source,destination);
        return destination;
    }
}
