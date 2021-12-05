package com.coffepotato.vo.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignInVO {

    private String UserId;

    private String Password;

}
