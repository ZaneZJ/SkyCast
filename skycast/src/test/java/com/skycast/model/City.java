package com.skycast.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "city")
@Data
public class City {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "population")
    private Integer population;

    @Column(name = "timezone")
    private Integer timezone;

    @Column(name = "sunrise")
    private Long sunrise;

    @Column(name = "sunset")
    private Long sunset;

}
