package com.coffepotato.db.dao;


import com.coffepotato.db.entity.TokenProvideServices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenProvideServicesRepository extends
                                        JpaRepository<TokenProvideServices, Long> {

    TokenProvideServices findByProvideServiceUrlEquals(String service_url);

    List<TokenProvideServices> findByProvideStateEqualsOrderByProvideServiceId(short state);
}