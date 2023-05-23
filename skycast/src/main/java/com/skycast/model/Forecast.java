package com.skycast.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "forecast")
@Data
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "dt")
    private Long dt;

    @NotNull
    @Column(name = "temp")
    private BigDecimal temp;

    @NotNull
    @Column(name = "feels_like")
    private BigDecimal feelsLike;

    @NotNull
    @Column(name = "temp_min")
    private BigDecimal tempMin;

    @NotNull
    @Column(name = "temp_max")
    private BigDecimal tempMax;

    @NotNull
    @Column(name = "pressure")
    private Integer pressure;

    @NotNull
    @Column(name = "sea_level")
    private Integer seaLevel;

    @NotNull
    @Column(name = "grnd_level")
    private Integer grndLevel;

    @NotNull
    @Column(name = "humidity")
    private Integer humidity;

    @NotNull
    @Column(name = "temp_kf")
    private BigDecimal tempKf;

    @NotNull
    @Column(name = "weather_id")
    private Integer weatherId;

    @NotBlank
    @Column(name = "weather_main")
    private String weatherMain;

    @NotBlank
    @Column(name = "weather_description")
    private String weatherDescription;

    @NotBlank
    @Column(name = "weather_icon")
    private String weatherIcon;

    @NotNull
    @Column(name = "clouds_all")
    private Integer cloudsAll;

    @NotNull
    @Column(name = "wind_speed")
    private BigDecimal windSpeed;

    @NotNull
    @Column(name = "wind_deg")
    private Integer windDeg;

    @NotNull
    @Column(name = "wind_gust")
    private BigDecimal windGust;

    @NotNull
    @Column(name = "visibility")
    private Integer visibility;

    @NotNull
    @Column(name = "pop")
    private BigDecimal pop;

    @NotBlank
    @Column(name = "sys_pod")
    private String sysPod;

    @NotNull
    @Column(name = "dt_txt")
    private LocalDateTime dtTxt;



}
