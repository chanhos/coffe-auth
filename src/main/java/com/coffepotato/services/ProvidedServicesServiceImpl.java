package com.coffepotato.services;

import com.coffepotato.db.dao.TokenProvideServicesRepository;
import com.coffepotato.vo.common.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvidedServicesServiceImpl implements ProvidedServicesService {

    private final TokenProvideServicesRepository tokenProvideServicesRepository;

    @Autowired
    public ProvidedServicesServiceImpl(TokenProvideServicesRepository tokenProvideServicesRepository){
        this.tokenProvideServicesRepository = tokenProvideServicesRepository;
    }

    @Override
    public ResponseVo getProvidedServicesAll() {

        ResponseVo res = new ResponseVo();


        return res;
    }
}
