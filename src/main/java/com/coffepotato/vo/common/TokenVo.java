package com.coffepotato.vo.common;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class TokenVo {

    private String token;

    private String auth_type;

    private long expires_in;

    @Builder
    public TokenVo(String token, String auth_type, long expires_in) {
        this.token = token;
        this.auth_type = auth_type;
        this.expires_in = expires_in;
    }
}
