package com.totoro.AntiAbuse.demo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Getter
@Builder
public class DemoDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public DemoDomain(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected DemoDomain() {

    }
    public static DemoDomain generateDemo(Long id, String name){
        DemoDomain domain = DemoDomain.builder()
                .id(id)
                .name(name)
                .build();
        return domain;
    }
}
