package com.skycast.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skycast.exception.CityAlreadyExistsException;
import com.skycast.exception.CityNotFoundException;
import com.skycast.model.City;
import com.skycast.repo.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class CityService {

    private CityRepo cityRepo;

    @Autowired
    public CityService (CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public City importCityData(String jsonData, String ip) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode citiesJsonArray = objectMapper.readTree(jsonData);

        // Extract values from the JSON data
        JsonNode cityNode = citiesJsonArray.get(0);
        BigDecimal lon = cityNode.get("lon").decimalValue();
        BigDecimal lat = cityNode.get("lat").decimalValue();
        String name = cityNode.get("name").asText().toLowerCase();
        String country = cityNode.get("country").asText().toLowerCase();

        // Create a new City object with the extracted values
        City city = new City();
        city.setName(name);
        city.setLatitude(lat);
        city.setLongitude(lon);
        city.setCountry(country);
        city.setIp(ip);

        // Save the city data
        return addCity(city);
    }

    @Cacheable("cityByNameAndCountry")
    public City getCityByNameAndCountry(String name, String country) throws CityNotFoundException {
        return cityRepo.findByNameAndCountry(name, country)
                .orElseThrow(() -> new CityNotFoundException("City and country not found for name: " + name + " and country: " + country));
    }

    @Cacheable("cityByName")
    public City getCityByName(String name) throws CityNotFoundException {
        return cityRepo.findByName(name)
                .orElseThrow(() -> new CityNotFoundException("City not found for name: " + name));
    }

    public City addCity(City city) throws CityAlreadyExistsException {
        if (cityRepo.existsByNameAndCountry(city.getName(), city.getCountry())) {
            throw new CityAlreadyExistsException("City already exists for name: " + city.getName() + " and country: " + city.getCountry());
        }
        return cityRepo.save(city);
    }

    public void deleteCity(String name, String country) throws CityNotFoundException {
        City city = getCityByNameAndCountry(name, country);
        cityRepo.delete(city);
    }

}
