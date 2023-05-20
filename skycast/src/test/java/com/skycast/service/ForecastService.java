package com.skycast.service;

import com.skycast.exception.ForecastAlreadyExistsException;
import com.skycast.exception.ForecastNotFoundException;
import com.skycast.model.Forecast;
import com.skycast.repo.ForecastRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.List;

public class ForecastService {

    private final ForecastRepo forecastRepo;

    @Autowired
    public ForecastService(ForecastRepo forecastRepo) {
        this.forecastRepo = forecastRepo;
    }

//    public Forecast getForecastByDtTxt(String dtTxt) throws ServerException {
//        return forecastRepo.findByDtTxt(dtTxt)
//                .orElseThrow(() -> new ForecastNotFoundException("Forecast not found for dtTxt: " + dtTxt));
//    }
//
//    public void addForecast(Forecast forecast) throws ForecastAlreadyExistsException {
//        String dtTxt = forecast.getDtTxt();
//        if (forecastRepo.existsByDtTxt(dtTxt)) {
//            throw new ForecastAlreadyExistsException("Forecast already exists for dtTxt: " + dtTxt);
//        }
//        forecastRepo.save(forecast);
//    }
//
//    public void updateForecast(String dtTxt, Forecast updatedForecast) throws ForecastNotFoundException {
//        Forecast existingForecast = getForecastByDtTxt(dtTxt);
//        existingForecast.setMain(updatedForecast.getMain());
//        existingForecast.setWeather(updatedForecast.getWeather());
//        // Update other fields as needed
//        forecastRepo.save(existingForecast);
//    }
//
//    public void deleteForecast(String dtTxt) throws ForecastNotFoundException {
//        Forecast existingForecast = getForecastByDtTxt(dtTxt);
//        forecastRepo.delete(existingForecast);
//    }

    public Forecast getForecastByDtTxt(LocalDateTime dtTxt) throws ForecastNotFoundException {
        return forecastRepo.findByDtTxt(dtTxt)
                .orElseThrow(() -> new ForecastNotFoundException("Forecast not found for dtTxt: " + dtTxt));
    }

    public List<Forecast> getAllForecasts() {
        return forecastRepo.findAll();
    }

    public Forecast addForecast(Forecast forecast) throws ForecastAlreadyExistsException {
        if (forecastRepo.existsByDtTxt(forecast.getDtTxt())) {
            throw new ForecastAlreadyExistsException("Forecast already exists for dtTxt: " + forecast.getDtTxt());
        }
        return forecastRepo.save(forecast);
    }

    public Forecast updateForecast(LocalDateTime dtTxt, Forecast forecast) throws ForecastNotFoundException {
        Forecast existingForecast = getForecastByDtTxt(dtTxt);
        existingForecast.setTemp(forecast.getTemp());
        existingForecast.setFeelsLike(forecast.getFeelsLike());
        existingForecast.setTempMin(forecast.getTempMin());
        existingForecast.setTempMax(forecast.getTempMax());
        // Set other properties as needed

        return forecastRepo.save(existingForecast);
    }

    public void deleteForecast(LocalDateTime dtTxt) throws ForecastNotFoundException {
        Forecast forecast = getForecastByDtTxt(dtTxt);
        forecastRepo.delete(forecast);
    }

}
