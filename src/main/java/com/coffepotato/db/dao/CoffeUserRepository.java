package com.coffepotato.db.dao;

import com.coffepotato.db.entity.CoffeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface CoffeUserRepository extends
        JpaRepository<CoffeUser, Long> {

        public CoffeUser findCoffeUserByUserIdOrderByKorNameAsc(String userId);

}