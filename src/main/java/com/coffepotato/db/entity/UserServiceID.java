package com.coffepotato.db.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class UserServiceID implements Serializable {


    @EqualsAndHashCode.Include
    @Column(name = "user_id")
    private String  userId;


    @EqualsAndHashCode.Include
    @Column(name = "auth_service_url")
    private String authServiceUrl;

    public UserServiceID(String userId, String authServiceUrl){
        this.userId = userId;
        this.authServiceUrl = authServiceUrl;
    }
}
