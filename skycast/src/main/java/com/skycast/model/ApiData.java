package com.skycast.model;

import lombok.Data;

@Data
public class ApiData {

    City city;
    Weather weather;
    Forecast forecast;

    public ApiData(City city, Weather weather, Forecast forecast) {
        this.city = city;
        this.weather = weather;
        this.forecast = forecast;
    }

}
