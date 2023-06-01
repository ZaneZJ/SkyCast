package com.skycast.service;

import com.skycast.WeatherAPI;
import com.skycast.exception.CityNotFoundException;
import com.skycast.exception.WeatherNotFoundException;
import com.skycast.model.ApiData;
import com.skycast.model.City;
import com.skycast.model.Forecast;
import com.skycast.model.Weather;
import com.skycast.repo.CityRepo;
import com.skycast.repo.ForecastRepo;
import com.skycast.repo.WeatherRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApiService {

    private static final String DATE_TIME_FIELD = "dt_txt";
    public static final String LIST_FIELD = "list";

    WeatherAPI weatherAPI;
    CityService cityService;
    ForecastService forecastService;
    WeatherService weatherService;
    WeatherRepo weatherRepo;
    CityRepo cityRepo;
    ForecastRepo forecastRepo;

    @Autowired
    public ApiService(WeatherAPI weatherAPI, CityService cityService,
                      CityRepo cityRepo,
                      ForecastRepo forecastRepo,
                      ForecastService forecastService,
                      WeatherService weatherService,
                      WeatherRepo weatherRepo) {
        this.weatherAPI = weatherAPI;
        this.cityService = cityService;
        this.cityRepo = cityRepo;
        this.forecastRepo = forecastRepo;
        this.forecastService = forecastService;
        this.weatherService = weatherService;
        this.weatherRepo = weatherRepo;
    }

    @Cacheable("data")
    public ApiData getApiAllData(HttpServletRequest request) throws IOException {

        City city = null;
        List<Forecast> forecasts = new ArrayList<>();
        Weather weatherData = null;

        // By calling this function on accessing the page
        // We read the IP either from the browser if not on the localhost
        // Or from the backend if the app is run on the localhost
        String ipAddress = weatherAPI.getIPData(request);

        // Getting city as lowercase using the IP
        String cityName = weatherAPI.getCity(ipAddress);
        System.out.println(cityName);

        // Getting city data
        String jsonResponseCity = weatherAPI.getCityJsonResponseFromAPI(cityName);

        if (jsonResponseCity != null) {

            // Getting latitude and longitude using city
            double[] latLonArray = getLatAndLong(jsonResponseCity);
            if (!cityRepo.findByName(cityName).isPresent()) {
                // Updating city data to DB
                cityService.importCityData(jsonResponseCity, ipAddress);
            }
            city = cityRepo.findByName(cityName).orElseThrow(() -> new CityNotFoundException());

            forecasts = findForecastsForCity(city, latLonArray);

            weatherData = findWeatherForCity(city, cityName);

        } else {
            // Get the city data
            city = cityRepo.findByName(cityName).orElseThrow(() -> new CityNotFoundException());
        }

        return new ApiData(city, weatherData, forecasts);
    }

    Weather findWeatherForCity(City city, String cityName) throws IOException {
        Weather weather;
        // Getting current weather data
        String jsonResponseWeather = weatherAPI.getWeatherDataFromAPI(cityName);

        if (jsonResponseWeather != null) {

            JSONObject jsonObject = new JSONObject(jsonResponseWeather);
            String dt = Long.toString(jsonObject.getLong("dt"));
            String weatherDateTime = weatherService.extractDate(weatherService.convertDateFormat(dt));

            if (weatherRepo.findByDtAndCity(weatherDateTime, city).isPresent()) {
                weatherService.updateWeather(weatherDateTime, jsonResponseWeather, city);
            } else {
                weatherService.importWeatherData(jsonResponseWeather, city);
            }

            // Get the weather data where dt matches today's date
            weather = weatherRepo.findByDtAndCity(LocalDate.now().toString(), city)
                    .orElseThrow(() -> new WeatherNotFoundException("Weather not found for today and city"));

            // Print Weather
            System.out.println("Weather: " + weather);

        } else {
            // Get the weather data where dt matches today's date
            weather = weatherRepo.findByDtAndCity(LocalDate.now().toString(), city)
                    .orElseThrow(() -> new WeatherNotFoundException("Weather not found for today and city"));
        }
        return weather;
    }

    List<Forecast> findForecastsForCity(City city, double[] latLonArray) throws IOException {
        List<Forecast> forecasts = new ArrayList<>();
        // Getting Forecast data for the next 5 days / 3 h forecast data
        String jsonResponseForecast = weatherAPI.getForecastDataFromAPI(latLonArray[0], latLonArray[1]);

        if (jsonResponseForecast != null) {

            JSONObject jsonObject = new JSONObject(jsonResponseForecast);
            JSONArray listArray = jsonObject.getJSONArray(LIST_FIELD);

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject listItem = listArray.getJSONObject(i);
                String dtTxt = listItem.getString(DATE_TIME_FIELD);
                System.out.println(dtTxt);

                if (forecastRepo.findByDtTxtAndCity(dtTxt, city).isPresent()) {
                    forecastService.updateForecast(dtTxt, jsonResponseForecast, i, city);
                } else {
                    forecastService.importForecastData(jsonResponseForecast, city, i);
                }

            }

            // Create a forecast array from today up to 5 days plus
            LocalDate currentDate = LocalDate.now();
            LocalDate targetDate = currentDate.plusDays(5);

            for (Forecast forecast : forecastRepo.findAll()) {
                LocalDate forecastDate = LocalDate.parse(forecast.getDtTxt().substring(0, 10));
                if (forecastDate.isEqual(currentDate) || (forecastDate.isAfter(currentDate) && forecastDate.isBefore(targetDate))) {
                    forecasts.add(forecast);
                }
            }

            // Print the found forecast data
            for (Forecast forecast : forecasts) {
                System.out.println("Forecast: " + forecast);
            }

        } else {
            // Create a forecast array from today up to 5 days plus
            LocalDate currentDate = LocalDate.now();
            LocalDate targetDate = currentDate.plusDays(5);

            for (Forecast forecast : forecastRepo.findAll()) {
                LocalDate forecastDate = LocalDate.parse(forecast.getDtTxt().substring(0, 10));
                if (forecastDate.isEqual(currentDate) || (forecastDate.isAfter(currentDate) && forecastDate.isBefore(targetDate))) {
                    forecasts.add(forecast);
                }
            }
        }
        return forecasts;
    }

    private double[] getLatAndLong(String response) {

        // Parse JSON response
        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() > 0) {
            JSONObject location = jsonArray.getJSONObject(0);
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");
            System.out.println("Latitude: " + lat);
            System.out.println("Longitude: " + lon);
            // Read Forecast Data
            double[] latLonArray = {lat, lon};
            return latLonArray;
        }
        return null;
    }

}
