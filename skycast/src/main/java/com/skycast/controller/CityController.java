package com.skycast.controller;

import com.skycast.WeatherAPI;
import com.skycast.model.City;
import com.skycast.repo.CityRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city")
public class CityController {

    CityRepo cityRepo;
    WeatherAPI weatherAPI;

    @Autowired
    public CityController(WeatherAPI weatherAPI, CityRepo cityRepo) {
        this.cityRepo = cityRepo;
        this.weatherAPI = weatherAPI;
    }

    @GetMapping("/test")
    public Integer addTestData() {
        City city = new City();
        city.setName("riga");
        city.setCountry("Latvia");
        City savedCity = cityRepo.save(city);
        return savedCity.getId();
    }

    @GetMapping("/weather")
    public void getApiData(HttpServletRequest request) {

        String ipAddress = weatherAPI.getIPData(request);

        String city = weatherAPI.getCity(ipAddress);
        double[] latLonArray = weatherAPI.getLatAndLonDataFromAPI(city);

        weatherAPI.getForecastDataFromAPI(latLonArray[0], latLonArray[1]);
        weatherAPI.getWeatherDataFromAPI(city);
    }

}
