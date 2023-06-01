package com.skycast.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiData {

    City city;
    Weather weather;
    List<Forecast> forecastArray;

}
