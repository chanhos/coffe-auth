package com.coffepotato.dto;

import lombok.*;


import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
public class ProvidedTokenServicesDTO {

    private Long provided_service_id;

    private String provided_service_url;

    private short provided_state;

    private String access_level;

    private Date update_date;

    @Builder
    public ProvidedTokenServicesDTO(Long provided_service_id, String provided_service_url, short provided_state, String access_level, Date update_date) {
        this.provided_service_id = provided_service_id;
        this.provided_service_url = provided_service_url;
        this.provided_state = provided_state;
        this.access_level = access_level;
        this.update_date = update_date;
    }
}
