package com.skycast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skycast.exception.WeatherAlreadyExistsException;
import com.skycast.exception.WeatherNotFoundException;
import com.skycast.model.City;
import com.skycast.model.Weather;
import com.skycast.repo.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherService {

    private final WeatherRepo weatherRepo;

    @Autowired
    public WeatherService(WeatherRepo weatherRepo) {
        this.weatherRepo = weatherRepo;
    }

    public Weather addWeather(Weather weather) throws WeatherAlreadyExistsException {
        String dt = weather.getDt();
        City city = weather.getCity();
        if (weatherRepo.existsByDtAndCity(dt, city)) {
            throw new WeatherAlreadyExistsException("Weather already exists for the given datetime and city");
        }
        return weatherRepo.save(weather);
    }

    public Weather getWeatherByDtAndCity(String dt, City city) throws WeatherNotFoundException {
        return weatherRepo.findByDtAndCity(dt, city)
                .orElseThrow(() -> new WeatherNotFoundException("Weather not found for dt: " + dt + " and city: " + city.getName()));
    }

    public boolean existsWeatherByDtAndCity(String dt, City city) {
        return weatherRepo.existsByDtAndCity(dt, city);
    }

    public Weather importWeatherData(String jsonData, City cityData) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode weatherNode = objectMapper.readTree(jsonData);

        // Extract values from the JSON data
        String dt = extractDate(convertDateFormat(weatherNode.get("dt").asText()));
        String weatherDescription = weatherNode.get("weather").get(0).get("description").asText();
        String weatherIcon = weatherNode.get("weather").get(0).get("icon").asText();
        BigDecimal temp = weatherNode.get("main").get("temp").decimalValue();
        BigDecimal feelsLike = weatherNode.get("main").get("feels_like").decimalValue();
        BigDecimal tempMin = weatherNode.get("main").get("temp_min").decimalValue();
        BigDecimal tempMax = weatherNode.get("main").get("temp_max").decimalValue();
        Integer pressure = weatherNode.get("main").get("pressure").asInt();
        Integer humidity = weatherNode.get("main").get("humidity").asInt();
        BigDecimal windSpeed = weatherNode.get("wind").get("speed").decimalValue();
        Integer windDeg = weatherNode.get("wind").get("deg").asInt();
        Integer cloudsAll = weatherNode.get("clouds").get("all").asInt();
        String sysSunrise = convertDateFormat(weatherNode.get("sys").get("sunrise").asText());
        String sysSunset = convertDateFormat(weatherNode.get("sys").get("sunset").asText());

        // Create a new Weather object with the extracted values
        Weather weather = new Weather();
        weather.setDt(dt);
        weather.setWeatherDescription(weatherDescription);
        weather.setWeatherIcon(weatherIcon);
        weather.setTemp(temp);
        weather.setFeelsLike(feelsLike);
        weather.setTempMin(tempMin);
        weather.setTempMax(tempMax);
        weather.setPressure(pressure);
        weather.setHumidity(humidity);
        weather.setWindSpeed(windSpeed);
        weather.setWindDeg(windDeg);
        weather.setCloudsAll(cloudsAll);
        weather.setSysSunrise(sysSunrise);
        weather.setSysSunset(sysSunset);
        weather.setCity(cityData);

        // Save the weather data
        return addWeather(weather);
    }

    public Weather updateWeather(String dt, String jsonData, City city) throws JsonProcessingException, WeatherNotFoundException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode weatherNode = objectMapper.readTree(jsonData);

            Weather existingWeather = getWeatherByDtAndCity(dt, city);

            existingWeather.setWeatherDescription(weatherNode.get("weather").get(0).get("description").asText());
            existingWeather.setWeatherIcon(weatherNode.get("weather").get(0).get("icon").asText());
            existingWeather.setTemp(weatherNode.get("main").get("temp").decimalValue());
            existingWeather.setFeelsLike(weatherNode.get("main").get("feels_like").decimalValue());
            existingWeather.setTempMin(weatherNode.get("main").get("temp_min").decimalValue());
            existingWeather.setTempMax(weatherNode.get("main").get("temp_max").decimalValue());
            existingWeather.setPressure(weatherNode.get("main").get("pressure").asInt());
            existingWeather.setHumidity(weatherNode.get("main").get("humidity").asInt());
            existingWeather.setWindSpeed(weatherNode.get("wind").get("speed").decimalValue());
            existingWeather.setWindDeg(weatherNode.get("wind").get("deg").asInt());
            existingWeather.setCloudsAll(weatherNode.get("clouds").get("all").asInt());
            existingWeather.setSysSunrise(convertDateFormat(weatherNode.get("sys").get("sunrise").asText()));
            existingWeather.setSysSunset(convertDateFormat(weatherNode.get("sys").get("sunset").asText()));

            return weatherRepo.save(existingWeather);
        } catch (JsonProcessingException exception) {
            System.out.println("Failed to load weather JSON: " + jsonData);
            throw exception;
        }
    }

    public String convertDateFormat(String unixTimestampString) {

        // Convert Unix timestamp string to long
        long unixTimestamp = Long.parseLong(unixTimestampString);

        // Convert Unix timestamp to LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(unixTimestamp),
                ZoneId.systemDefault()
        );

        // Format the LocalDateTime as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);

        // Print the formatted date and time as a string
        System.out.println("Formatted DateTime: " + formattedDateTime);

        return formattedDateTime;
    }

    public String extractDate(String dt) {

        LocalDateTime dateTime = LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String date = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        System.out.println("Formatted Date: " + date);

        return date;

    }

}
