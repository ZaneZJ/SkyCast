package com.skycast.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "weather")
@Data
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "dt")
    private Long dt;

    @NotBlank
    @Column(name = "weather_description")
    private String weatherDescription;

    @NotBlank
    @Column(name = "weather_icon")
    private String weatherIcon;

    @NotNull
    @Column(name = "temp")
    private Double temp;

    @NotNull
    @Column(name = "feels_like")
    private Double feelsLike;

    @NotNull
    @Column(name = "temp_min")
    private Double tempMin;

    @NotNull
    @Column(name = "temp_max")
    private Double tempMax;

    @NotNull
    @Column(name = "pressure")
    private Integer pressure;

    @NotNull
    @Column(name = "humidity")
    private Integer humidity;

    @NotNull
    @Column(name = "wind_speed")
    private Double windSpeed;

    @NotNull
    @Column(name = "wind_deg")
    private Integer windDeg;

    @NotNull
    @Column(name = "clouds_all")
    private Integer cloudsAll;

    @NotNull
    @Column(name = "sys_sunrise")
    private Long sysSunrise;

    @NotNull
    @Column(name = "sys_sunset")
    private Long sysSunset;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

}
