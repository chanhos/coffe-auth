package com.coffepotato.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "sys_refresh_token_keeper" , catalog = "test_db")
@Entity
public class RefreshTokenKeeper {

    @Id
    @Column(name = "token_id", nullable = false)
    private String tokenId;

    @Column(name = "token_owner" , nullable = false)
    private String tokenOwner;

    @Column(name ="token_issued_state" , columnDefinition = "BIT")
    private short tokenState;

    @Column(name = "token_created_at" )
    private Date createAt;

    @Column(name = "token_expire_date")
    private Date expireOn;

    @Column(name ="token_text" , columnDefinition = "TEXT")
    private String token;

}
