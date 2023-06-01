package com.skycast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skycast.exception.ForecastAlreadyExistsException;
import com.skycast.exception.ForecastNotFoundException;
import com.skycast.model.City;
import com.skycast.model.Forecast;
import com.skycast.repo.ForecastRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForecastServiceTest {

    private ForecastService forecastService;

    @Mock
    private ForecastRepo forecastRepo;

    @BeforeEach
    public void setup() {
        forecastService = new ForecastService(forecastRepo);
    }

    @Test
    public void importForecastData_ShouldReturnSavedForecast() throws IOException {
        String jsonData = "{\"list\":[{\"dt\":1234567890,\"main\":{\"temp\":10.0,\"feels_like\":8.0,\"temp_min\":5.0,\"temp_max\":15.0,\"pressure\":1000,\"sea_level\":1000,\"grnd_level\":1000,\"humidity\":80,\"temp_kf\":0.5},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.5,\"deg\":180, \"gust\":95},\"visibility\":10000,\"dt_txt\":\"2023-01-01 12:00:00\"}]}";
        City cityData = new City();
        Integer id = 0;

        Forecast expectedForecast = new Forecast();
        expectedForecast.setDt(1234567890L);
        expectedForecast.setTemp(BigDecimal.valueOf(10.0));
        expectedForecast.setFeelsLike(BigDecimal.valueOf(8.0));
        expectedForecast.setTempMin(BigDecimal.valueOf(5.0));
        expectedForecast.setTempMax(BigDecimal.valueOf(15.0));
        expectedForecast.setPressure(1000);
        expectedForecast.setSeaLevel(1000);
        expectedForecast.setGrndLevel(1000);
        expectedForecast.setHumidity(80);
        expectedForecast.setTempKf(BigDecimal.valueOf(0.5));
        expectedForecast.setWeatherId(id);
        expectedForecast.setWeatherMain("Clear");
        expectedForecast.setWeatherDescription("clear sky");
        expectedForecast.setWeatherIcon("01d");
        expectedForecast.setCloudsAll(0);
        expectedForecast.setWindSpeed(BigDecimal.valueOf(2.5));
        expectedForecast.setWindDeg(180);
        expectedForecast.setWindGust(BigDecimal.valueOf(95));
        expectedForecast.setVisibility(10000);
        expectedForecast.setDtTxt("2023-01-01 12:00:00");
        expectedForecast.setCity(cityData);

        when(forecastRepo.save(expectedForecast)).thenReturn(expectedForecast);

        Forecast actualForecast = forecastService.importForecastData(jsonData, cityData, id);

        assertThat(actualForecast).isEqualTo(expectedForecast);
        verify(forecastRepo).save(expectedForecast);
    }

    @Test
    public void getForecastByDtTxtAndCity_WhenForecastExists_ShouldReturnForecast() throws ForecastNotFoundException {
        String dtTxt = "2023-01-01 12:00:00";
        City city = new City();
        Forecast expectedForecast = new Forecast();

        when(forecastRepo.findByDtTxtAndCity(dtTxt, city)).thenReturn(Optional.of(expectedForecast));

        Forecast actualForecast = forecastService.getForecastByDtTxtAndCity(dtTxt, city);

        assertThat(actualForecast).isEqualTo(expectedForecast);
        verify(forecastRepo).findByDtTxtAndCity(dtTxt, city);
    }

    @Test
    public void getForecastByDtTxtAndCity_WhenForecastDoesNotExist_ShouldThrowForecastNotFoundException() {
        String dtTxt = "2023-01-01 12:00:00";
        City city = new City();

        when(forecastRepo.findByDtTxtAndCity(dtTxt, city)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> forecastService.getForecastByDtTxtAndCity(dtTxt, city))
                .isInstanceOf(ForecastNotFoundException.class)
                .hasMessageContaining("Forecast not found");
        verify(forecastRepo).findByDtTxtAndCity(dtTxt, city);
    }

    @Test
    public void getAllForecasts_ShouldReturnAllForecasts() {
        List<Forecast> forecastList = new ArrayList<>();
        Forecast forecast1 = new Forecast();
        Forecast forecast2 = new Forecast();
        forecastList.add(forecast1);
        forecastList.add(forecast2);

        when(forecastRepo.findAll()).thenReturn(forecastList);

        List<Forecast> actualForecasts = forecastService.getAllForecasts();

        assertThat(actualForecasts).isEqualTo(forecastList);
        verify(forecastRepo).findAll();
    }

    @Test
    public void addForecast_WhenForecastDoesNotExist_ShouldReturnSavedForecast() throws ForecastAlreadyExistsException {
        Forecast forecast = new Forecast();

        when(forecastRepo.existsByDtTxtAndCity(forecast.getDtTxt(), forecast.getCity())).thenReturn(false);
        when(forecastRepo.save(forecast)).thenReturn(forecast);

        Forecast actualForecast = forecastService.addForecast(forecast);

        assertThat(actualForecast).isEqualTo(forecast);
        verify(forecastRepo).existsByDtTxtAndCity(forecast.getDtTxt(), forecast.getCity());
        verify(forecastRepo).save(forecast);
    }

    @Test
    public void addForecast_WhenForecastExists_ShouldThrowForecastAlreadyExistsException() {
        Forecast forecast = new Forecast();

        when(forecastRepo.existsByDtTxtAndCity(forecast.getDtTxt(), forecast.getCity())).thenReturn(true);

        assertThatThrownBy(() -> forecastService.addForecast(forecast))
                .isInstanceOf(ForecastAlreadyExistsException.class)
                .hasMessageContaining("Forecast already exists");
        verify(forecastRepo).existsByDtTxtAndCity(forecast.getDtTxt(), forecast.getCity());
    }

    @Test
    public void updateForecast_ShouldReturnUpdatedForecast() throws ForecastNotFoundException, JsonProcessingException {
        String dtTxt = "2023-01-01 12:00:00";
        String jsonData = "{\"list\":[{\"dt\":1234567890,\"main\":{\"temp\":10.0,\"feels_like\":8.0,\"temp_min\":5.0,\"temp_max\":15.0,\"pressure\":1000,\"sea_level\":1000,\"grnd_level\":1000,\"humidity\":80,\"temp_kf\":0.5},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.5,\"deg\":180, \"gust\":95, \"gust\":95},\"visibility\":10000}]}";
        Integer id = 0;
        City city = new City();
        Forecast existingForecast = new Forecast();

        existingForecast.setTemp(BigDecimal.valueOf(10.0));
        existingForecast.setFeelsLike(BigDecimal.valueOf(8.0));
        existingForecast.setTempMin(BigDecimal.valueOf(5.0));
        existingForecast.setTempMax(BigDecimal.valueOf(15.0));
        existingForecast.setPressure(1000);
        existingForecast.setSeaLevel(1000);
        existingForecast.setGrndLevel(1000);
        existingForecast.setHumidity(80);
        existingForecast.setTempKf(BigDecimal.valueOf(0.5));
        existingForecast.setWeatherId(id);
        existingForecast.setWeatherMain("Clear");
        existingForecast.setWeatherDescription("clear sky");
        existingForecast.setWeatherIcon("01d");
        existingForecast.setCloudsAll(0);
        existingForecast.setWindSpeed(BigDecimal.valueOf(2.5));
        existingForecast.setWindDeg(180);
        existingForecast.setWindGust(BigDecimal.valueOf(95));
        existingForecast.setVisibility(10000);

        when(forecastRepo.findByDtTxtAndCity(dtTxt, city)).thenReturn(Optional.of(existingForecast));
        when(forecastRepo.save(existingForecast)).thenReturn(existingForecast);

        Forecast actualForecast = forecastService.updateForecast(dtTxt, jsonData, id, city);

        assertThat(actualForecast).isEqualTo(existingForecast);
        verify(forecastRepo).save(existingForecast);
    }

    @Test
    public void deleteForecast_ShouldCallForecastRepoDelete() throws ForecastNotFoundException {
        String dtTxt = "2023-01-01 12:00:00";
        City city = new City();
        Forecast forecast = new Forecast();

        when(forecastRepo.findByDtTxtAndCity(dtTxt, city)).thenReturn(Optional.of(forecast));

        forecastService.deleteForecast(dtTxt, city);

        verify(forecastRepo).delete(forecast);
    }

    @Test
    public void deleteForecast_WhenForecastDoesNotExist_ShouldThrowForecastNotFoundException() {
        String dtTxt = "2023-01-01 12:00:00";
        City city = new City();

        when(forecastRepo.findByDtTxtAndCity(dtTxt, city)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> forecastService.deleteForecast(dtTxt, city))
                .isInstanceOf(ForecastNotFoundException.class);
    }
}