package com.example.demo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Table;

@Entity
//@Table(name = "tutorials")
public class PowerPlant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    String country; // 3 character country code corresponding to the ISO 3166-1 alpha-3 specification
    @Column(nullable = false)
    String country_long; //longer form of the country designation
    @Column(nullable = false)
    String name; //name or title of the power plant, generally in Romanized form
    @Column(nullable = false, unique = true)
    String gppd_idnr; //10 or 12 character identifier for the power plant
    @Column(nullable = false)
    int capacity_mw; //electrical generating capacity in megawatts
    @Column(nullable = false)
    double longitude; //geolocation in decimal degrees
    @Column(nullable = false)
    double latitude; //geolocation in decimal degrees
    @Column(nullable = false)
    String primary_fuel; //energy source used in primary electricity generation or export

    public PowerPlant() {
    }

    public PowerPlant(String country, String country_long, String name, String gppd_idnr, int capacity_mw,
            double longitude, double latitude, String primary_fuel) {
        this.country = country;
        this.country_long = country_long;
        this.name = name;
        this.gppd_idnr = gppd_idnr;
        this.capacity_mw = capacity_mw;
        this.longitude = longitude;
        this.latitude = latitude;
        this.primary_fuel = primary_fuel;
    }

    //getters and setters for all
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_long() {
        return country_long;
    }

    public void setCountry_long(String country_long) {
        this.country_long = country_long;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGppd_idnr() {
        return gppd_idnr;
    }

    public void setGppd_idnr(String gppd_idnr) {
        this.gppd_idnr = gppd_idnr;
    }

    public int getCapacity_mw() {
        return capacity_mw;
    }

    public void setCapacity_mw(int capacity_mw) {
        this.capacity_mw = capacity_mw;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPrimary_fuel() {
        return primary_fuel;
    }

    public void setPrimary_fuel(String primary_fuel) {
        this.primary_fuel = primary_fuel;
    }
}
