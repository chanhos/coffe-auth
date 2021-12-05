package com.coffepotato.dto;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenDTO {

    @NonNull
    private String tokenId = "";

    private String tokenOwner;

    private short tokenState;

    private Date createAt;

    private Date expireOn;

    private String token;

    @Builder
    public RefreshTokenDTO(@NonNull String tokenId, String tokenOwner, short tokenState
                                , Date createAt, Date expireOn, String token) {
        this.tokenId = tokenId;
        this.tokenOwner = tokenOwner;
        this.tokenState = tokenState;
        this.createAt = createAt;
        this.expireOn = expireOn;
        this.token = token;
    }
}
