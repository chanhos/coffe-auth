package com.coffepotato.vo.common;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ResponseVo {

    private int responseCode = 0;

    private String responseMsg = "Success";

    private Object data;

    @Builder
    public ResponseVo(int responseCode, String responseMsg) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }
}