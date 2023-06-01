package com.skycast.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "forecast")
@Data
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dt")
    private Long dt;

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

    @Column(name = "sea_level")
    private Integer seaLevel;

    @Column(name = "grnd_level")
    private Integer grndLevel;

    @Column(name = "humidity")
    private Integer humidity;

    @Column(name = "temp_kf")
    private BigDecimal tempKf;

    @Column(name = "weather_id")
    private Integer weatherId;

    @Column(name = "weather_main")
    private String weatherMain;

    @Column(name = "weather_description")
    private String weatherDescription;

    @Column(name = "weather_icon")
    private String weatherIcon;

    @Column(name = "clouds_all")
    private Integer cloudsAll;

    @Column(name = "wind_speed")
    private BigDecimal windSpeed;

    @Column(name = "wind_deg")
    private Integer windDeg;

    @Column(name = "wind_gust")
    private BigDecimal windGust;

    @Column(name = "visibility")
    private Integer visibility;

    @Column(name = "dt_txt")
    private String dtTxt;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

}
