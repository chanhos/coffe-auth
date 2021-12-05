package com.coffepotato.db.entity;


import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "tb_coffe_user_info" , catalog = "test_db")
@Entity
public class CoffeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no", nullable = false)
    private Long userNo;

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    @Column(name = "coffe_user_id", nullable = false, unique = true , length = 7)
    private String userId;

    @Column(name = "kor_name" , nullable = false)
    private String korName;

    @Column(name ="kor_nick_name" , nullable = false)
    private String nickName;

    @Column(name = "use_yn" , nullable = false, columnDefinition = "TINYINT")
    private boolean useYn;


    @Column(name = "coffe_pw" , nullable = false , length = 150)
    private String password;
}
