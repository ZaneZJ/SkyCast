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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiServiceTest {

    private ApiService apiService;

    @Mock
    private WeatherAPI weatherAPI;

    @Mock
    private CityService cityService;

    @Mock
    private ForecastService forecastService;

    @Mock
    private WeatherService weatherService;

    @Mock
    private WeatherRepo weatherRepo;

    @Mock
    private CityRepo cityRepo;

    @Mock
    private ForecastRepo forecastRepo;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        apiService = spy(new ApiService(weatherAPI, cityService, cityRepo, forecastRepo, forecastService, weatherService, weatherRepo));
    }

    @Test
    public void getApiAllData_ShouldReturnApiData() throws IOException {
        // Arrange
        when(weatherAPI.getIPData(request)).thenReturn("127.0.0.1");
        when(weatherAPI.getCity("127.0.0.1")).thenReturn("TestCity");
        when(weatherAPI.getCityJsonResponseFromAPI("TestCity")).thenReturn("[{\"lat\":123.321, \"lon\":321.123}]");


        City city = new City();
        when(cityRepo.findByName("TestCity")).thenReturn(Optional.of(city));

        List<Forecast> forecasts = new ArrayList<>();
        doReturn(forecasts).when(apiService).findForecastsForCity(city, new double[]{123.321, 321.123});

        Weather weather = new Weather();
        doReturn(weather).when(apiService).findWeatherForCity(city, "TestCity");

        // Act
        ApiData actualApiData = apiService.getApiAllData(request);

        // Assert
        ApiData expectedApiData = new ApiData(city, weather, forecasts);
        assertThat(actualApiData).isEqualTo(expectedApiData);
    }

    @Test
    public void getApiAllData_WhenCityNotFound_ShouldThrowCityNotFoundException() {
        // Arrange
        when(weatherAPI.getIPData(request)).thenReturn("127.0.0.1");
        when(weatherAPI.getCity("127.0.0.1")).thenReturn("TestCity");
        when(cityRepo.findByName("TestCity")).thenReturn(Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> apiService.getApiAllData(request))
                .isInstanceOf(CityNotFoundException.class);
    }

    @Test
    public void findWeatherForCity_WhenWeatherExistsAndFound_ShouldReturnWeather() throws IOException {
        // Arrange
        when(weatherAPI.getWeatherDataFromAPI("TestCity")).thenReturn("{\"dt\":\"123\"}");
        when(weatherService.convertDateFormat("123")).thenReturn("testDateFormat");
        when(weatherService.extractDate("testDateFormat")).thenReturn("testDate");

        City city = new City();

        Weather weather = new Weather();
        when(weatherRepo.findByDtAndCity(LocalDate.now().toString(), city)).thenReturn(Optional.of(weather));

        when(weatherRepo.findByDtAndCity("testDate", city)).thenReturn(Optional.of(weather));

        // Act
        Weather actualWeather = apiService.findWeatherForCity(city, "TestCity");

        // Assert
        assertThat(actualWeather).isEqualTo(weather);
        verify(weatherService).updateWeather("testDate", "{\"dt\":\"123\"}", city);
    }

    @Test
    public void findWeatherForCity_WhenWeatherNotExistsAndFound_ShouldReturnWeather() throws IOException {
        // Arrange
        when(weatherAPI.getWeatherDataFromAPI("TestCity")).thenReturn("{\"dt\":\"123\"}");
        when(weatherService.convertDateFormat("123")).thenReturn("testDateFormat");
        when(weatherService.extractDate("testDateFormat")).thenReturn("testDate");

        City city = new City();

        Weather weather = new Weather();
        when(weatherRepo.findByDtAndCity(LocalDate.now().toString(), city)).thenReturn(Optional.of(weather));

        when(weatherRepo.findByDtAndCity("testDate", city)).thenReturn(Optional.empty());

        // Act
        Weather actualWeather = apiService.findWeatherForCity(city, "TestCity");

        // Assert
        assertThat(actualWeather).isEqualTo(weather);
        verify(weatherService).importWeatherData("{\"dt\":\"123\"}", city);
    }

    @Test
    public void findWeatherForCity_WhenWeatherNotFound_ShouldThrowWeatherNotFoundException() {
        // Arrange
        City city = new City();

        when(weatherAPI.getWeatherDataFromAPI("TestCity")).thenReturn(null);

        when(weatherRepo.findByDtAndCity(LocalDate.now().toString(), city)).thenReturn(Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> apiService.findWeatherForCity(city, "TestCity"))
                .isInstanceOf(WeatherNotFoundException.class);
    }

    @Test
    public void findForecastsForCity_WhenForecastsFound_ShouldReturnForecasts() throws IOException {
        // Arrange
        String dateTime = LocalDate.now().toString();
        City city = new City();
        when(weatherAPI.getForecastDataFromAPI(123.321, 321.123)).thenReturn("{\"list\":[{\"dt_txt\": \""+ dateTime + "\"}]}");

        when(forecastRepo.findByDtTxtAndCity(dateTime, city)).thenReturn(Optional.empty());

        List<Forecast> forecastList = new ArrayList<>();
        Forecast forecast = new Forecast();
        forecast.setDtTxt(dateTime);
        forecastList.add(forecast);
        when(forecastRepo.findAll()).thenReturn(forecastList);

        // Act
        List<Forecast> actualForecasts = apiService.findForecastsForCity(city, new double[]{123.321, 321.123});

        // Assert
        assertThat(actualForecasts).isEqualTo(forecastList);
    }

    @Test
    public void findForecastsForCity_WhenForecastDataNotFound_ShouldReturnEmptyList() throws IOException {
        // Arrange
        City city = new City();
        when(weatherAPI.getForecastDataFromAPI(0.0, 0.0)).thenReturn(null);

        List<Forecast> forecastList = new ArrayList<>();
        when(forecastRepo.findAll()).thenReturn(forecastList);

        // Act
        List<Forecast> actualForecasts = apiService.findForecastsForCity(city, new double[]{0.0, 0.0});

        // Assert
        assertThat(actualForecasts).isEmpty();
    }
}
