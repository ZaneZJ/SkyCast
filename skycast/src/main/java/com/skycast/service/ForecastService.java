package com.skycast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skycast.exception.ForecastAlreadyExistsException;
import com.skycast.exception.ForecastNotFoundException;
import com.skycast.model.City;
import com.skycast.model.Forecast;
import com.skycast.repo.ForecastRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Component
public class ForecastService {

    private ForecastRepo forecastRepo;

    @Autowired
    public ForecastService(ForecastRepo forecastRepo) {
        this.forecastRepo = forecastRepo;
    }

    public Forecast importForecastData(String jsonData, City cityData, Integer id) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);

            // Extract values from the JSON data
            JsonNode forecastNode = rootNode.get("list").get(id);

            Long dt = forecastNode.get("dt").asLong();
            BigDecimal temp = forecastNode.get("main").get("temp").decimalValue();
            BigDecimal feelsLike = forecastNode.get("main").get("feels_like").decimalValue();
            BigDecimal tempMin = forecastNode.get("main").get("temp_min").decimalValue();
            BigDecimal tempMax = forecastNode.get("main").get("temp_max").decimalValue();
            Integer pressure = forecastNode.get("main").get("pressure").asInt();
            Integer seaLevel = forecastNode.get("main").get("sea_level").asInt();
            Integer grndLevel = forecastNode.get("main").get("grnd_level").asInt();
            Integer humidity = forecastNode.get("main").get("humidity").asInt();
            BigDecimal tempKf = forecastNode.get("main").get("temp_kf").decimalValue();
            Integer weatherId = forecastNode.get("weather").asInt();
            String weatherMain = forecastNode.get("weather").get(0).get("main").asText();
            String weatherDescription = forecastNode.get("weather").get(0).get("description").asText();
            String weatherIcon = forecastNode.get("weather").get(0).get("icon").asText();
            Integer cloudsAll = forecastNode.get("clouds").get("all").asInt();
            BigDecimal windSpeed = forecastNode.get("wind").get("speed").decimalValue();
            Integer windDeg = forecastNode.get("wind").get("deg").asInt();
            BigDecimal windGust = forecastNode.get("wind").get("gust").decimalValue();
            Integer visibility = forecastNode.get("visibility").asInt();
            String dtTxt = forecastNode.get("dt_txt").asText();

            // Create a new Forecast object with the extracted values
            Forecast forecast = new Forecast();
            forecast.setDt(dt);
            forecast.setTemp(temp);
            forecast.setFeelsLike(feelsLike);
            forecast.setTempMin(tempMin);
            forecast.setTempMax(tempMax);
            forecast.setPressure(pressure);
            forecast.setSeaLevel(seaLevel);
            forecast.setGrndLevel(grndLevel);
            forecast.setHumidity(humidity);
            forecast.setTempKf(tempKf);
            forecast.setWeatherId(weatherId);
            forecast.setWeatherMain(weatherMain);
            forecast.setWeatherDescription(weatherDescription);
            forecast.setWeatherIcon(weatherIcon);
            forecast.setCloudsAll(cloudsAll);
            forecast.setWindSpeed(windSpeed);
            forecast.setWindDeg(windDeg);
            forecast.setWindGust(windGust);
            forecast.setVisibility(visibility);
            forecast.setDtTxt(dtTxt);
            forecast.setCity(cityData);

            // Save the forecast data
            return addForecast(forecast);
        } catch (IOException exception) {
            System.out.println("Failed to load weather JSON: " + jsonData);
            throw exception;
        }
    }

    public Forecast getForecastByDtTxtAndCity(String dtTxt, City city) throws ForecastNotFoundException {
        return forecastRepo.findByDtTxtAndCity(dtTxt, city)
                .orElseThrow(() -> new ForecastNotFoundException("Forecast not found for dtTxt: " + dtTxt + " and city: " + city));
    }

    public List<Forecast> getAllForecasts() {
        return forecastRepo.findAll();
    }

    public Forecast addForecast(Forecast forecast) throws ForecastAlreadyExistsException {
        if (forecastRepo.existsByDtTxtAndCity(forecast.getDtTxt(), forecast.getCity())) {
            throw new ForecastAlreadyExistsException("Forecast already exists for dtTxt: " + forecast.getDtTxt() + " and city: " + forecast.getCity());
        }
        return forecastRepo.save(forecast);
    }

    public Forecast updateForecast(String dtTxt, String jsonData, Integer id, City city) throws ForecastNotFoundException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);

        Forecast existingForecast = getForecastByDtTxtAndCity(dtTxt, city);

        JsonNode forecastNode = rootNode.get("list").get(id);

        existingForecast.setTemp(forecastNode.get("main").get("temp").decimalValue());
        existingForecast.setFeelsLike(forecastNode.get("main").get("feels_like").decimalValue());
        existingForecast.setTempMin(forecastNode.get("main").get("temp_min").decimalValue());
        existingForecast.setTempMax(forecastNode.get("main").get("temp_max").decimalValue());
        existingForecast.setPressure(forecastNode.get("main").get("pressure").asInt());
        existingForecast.setSeaLevel(forecastNode.get("main").get("sea_level").asInt());
        existingForecast.setGrndLevel(forecastNode.get("main").get("grnd_level").asInt());
        existingForecast.setHumidity(forecastNode.get("main").get("humidity").asInt());
        existingForecast.setTempKf(forecastNode.get("main").get("temp_kf").decimalValue());
        existingForecast.setWeatherId(forecastNode.get("weather").asInt());
        existingForecast.setWeatherMain(forecastNode.get("weather").get(0).get("main").asText());
        existingForecast.setWeatherDescription(forecastNode.get("weather").get(0).get("description").asText());
        existingForecast.setWeatherIcon(forecastNode.get("weather").get(0).get("icon").asText());
        existingForecast.setCloudsAll(forecastNode.get("clouds").get("all").asInt());
        existingForecast.setWindSpeed(forecastNode.get("wind").get("speed").decimalValue());
        existingForecast.setWindDeg(forecastNode.get("wind").get("deg").asInt());
        existingForecast.setWindGust(forecastNode.get("wind").get("gust").decimalValue());
        existingForecast.setVisibility(forecastNode.get("visibility").asInt());

        return forecastRepo.save(existingForecast);
    }

    public void deleteForecast(String dtTxt, City city) throws ForecastNotFoundException {
        Forecast forecast = getForecastByDtTxtAndCity(dtTxt, city);
        forecastRepo.delete(forecast);
    }

}
