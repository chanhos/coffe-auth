package com.coffepotato.db.dao;

import com.coffepotato.db.entity.UserServiceAuth;
import com.coffepotato.db.entity.UserServiceID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserServiceAuthRepository extends JpaRepository<UserServiceAuth, UserServiceID> {

    public List<UserServiceAuth> findUserServiceAuthByIdUserIdEquals(String user_id);
}