package com.coffepotato.db.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "user_service_auth" , catalog = "test_db")
@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserServiceAuth {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserServiceID id;

    @Column(name = "auth_access_level")
    private String authAccessLevel ;

    public UserServiceAuth(UserServiceID id, String authAccessLevel) {
        this.id = id;
        this.authAccessLevel = authAccessLevel;
    }
}