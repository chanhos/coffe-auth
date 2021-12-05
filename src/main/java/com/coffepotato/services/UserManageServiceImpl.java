package com.coffepotato.services;

import com.coffepotato.db.dao.CoffeUserRepository;
import com.coffepotato.vo.common.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserManageServiceImpl implements UserManageService {

    private final CoffeUserRepository coffeUserRepository;

    @Autowired
    public  UserManageServiceImpl(CoffeUserRepository coffeUserRepository){
        this.coffeUserRepository = coffeUserRepository;
    }

    @Override
    public ResponseVo getUserList(String userId) {

        ResponseVo res = new ResponseVo();

        return res;
    }
}
