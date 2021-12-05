package com.coffepotato.db.entity;

import lombok.Getter;
import javax.persistence.*;
import java.util.Date;

@Getter
@Table(name = "sys_token_provide_services" , catalog = "test_db")
@Entity
public class TokenProvideServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provide_service_id", nullable = false)
    private Long provideServiceId;

    public Long getProvideServiceId() {
        return provideServiceId;
    }

    public void setProvideServiceId(Long provideServiceId) {
        this.provideServiceId = provideServiceId;
    }

    @Column(name = "provide_service_url" )
    private String provideServiceUrl;

    @Column(name = "provide_state" , columnDefinition = "BIT")
    private short provideState;

    @Column(name = "provide_access_level")
    private String priovideAccessLevel;

    @Column(name = "update_date")
    private Date update_date;
}

