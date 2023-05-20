package com.skycast.controller;

import com.skycast.exception.ForecastAlreadyExistsException;
import com.skycast.exception.ForecastNotFoundException;
import com.skycast.model.Forecast;
import com.skycast.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    private final ForecastService forecastService;

    @Autowired
    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

//    @GetMapping("/{dtTxt}")
//    public ResponseEntity<Forecast> getForecastByDtTxt(@PathVariable String dtTxt) {
//        try {
//            Forecast forecast = forecastService.getForecastByDtTxt(dtTxt);
//            return ResponseEntity.ok(forecast);
//        } catch (ForecastNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } catch (ServerException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<String> addForecast(@RequestBody Forecast forecast) {
//        try {
//            forecastService.addForecast(forecast);
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//        } catch (ForecastAlreadyExistsException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        }
//    }
//
//    @PutMapping("/{dtTxt}")
//    public ResponseEntity<String> updateForecast(@PathVariable String dtTxt, @RequestBody Forecast forecast) {
//        try {
//            forecastService.updateForecast(dtTxt, forecast);
//            return ResponseEntity.ok().build();
//        } catch (ForecastNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{dtTxt}")
//    public ResponseEntity<String> deleteForecast(@PathVariable String dtTxt) {
//        try {
//            forecastService.deleteForecast(dtTxt);
//            return ResponseEntity.ok().build();
//        } catch (ForecastNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }

    @PostMapping
    public Forecast addForecast(@RequestBody Forecast forecast) throws ForecastAlreadyExistsException {
        return forecastService.addForecast(forecast);
    }

    @PutMapping("/{dtTxt}")
    public Forecast updateForecast(@PathVariable LocalDateTime dtTxt, @RequestBody Forecast forecast) throws ForecastNotFoundException {
        return forecastService.updateForecast(dtTxt, forecast);
    }

    @DeleteMapping("/{dtTxt}")
    public void deleteForecast(@PathVariable LocalDateTime dtTxt) throws ForecastNotFoundException {
        forecastService.deleteForecast(dtTxt);
    }

    @GetMapping("/{dtTxt}")
    public Forecast getForecastByDtTxt(@PathVariable LocalDateTime dtTxt) throws ForecastNotFoundException {
        return forecastService.getForecastByDtTxt(dtTxt);
    }

    @GetMapping
    public List<Forecast> getAllForecasts() {
        return forecastService.getAllForecasts();
    }

}
