package com.coffepotato.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO {

    private Long userNo;

    private String userId;

    private String korName;

    private String nickName;

    private boolean useYn;

    private String password;

    @Builder
    public UserDTO(Long userNo, String userId, String korName, String nickName, boolean useYn) {
        this.userNo = userNo;
        this.userId = userId;
        this.korName = korName;
        this.nickName = nickName;
        this.useYn = useYn;
    }
}
