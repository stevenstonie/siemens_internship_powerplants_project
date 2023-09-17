package com.example.consumer.model;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class PowerPlant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gppd_idnr", nullable = false)
    private String gppd_idnr;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "country_long", nullable = false)
    private String country_long;

    @Column(nullable = false)
    private String name;

    @Column(name = "capacity_mw", nullable = false)
    private Double capacity_mw;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "primary_fuel", nullable = false)
    private String primary_fuel;
}
