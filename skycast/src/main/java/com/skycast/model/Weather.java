package com.skycast.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "weather")
@Data
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dt")
    private String dt;

    @Column(name = "weather_description")
    private String weatherDescription;

    @Column(name = "weather_icon")
    private String weatherIcon;

    @Column(name = "temp")
    private BigDecimal temp;

    @Column(name = "feels_like")
    private BigDecimal feelsLike;

    @Column(name = "temp_min")
    private BigDecimal tempMin;

    @Column(name = "temp_max")
    private BigDecimal tempMax;

    @Column(name = "pressure")
    private Integer pressure;

    @Column(name = "humidity")
    private Integer humidity;

    @Column(name = "wind_speed")
    private BigDecimal windSpeed;

    @Column(name = "wind_deg")
    private Integer windDeg;

    @Column(name = "clouds_all")
    private Integer cloudsAll;

    @Column(name = "sys_sunrise")
    private String sysSunrise;

    @Column(name = "sys_sunset")
    private String sysSunset;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

}
