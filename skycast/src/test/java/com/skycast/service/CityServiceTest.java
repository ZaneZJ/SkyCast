package com.skycast.service;

import com.skycast.exception.CityAlreadyExistsException;
import com.skycast.model.ApiData;
import com.skycast.model.City;
import com.skycast.repo.CityRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class})
class CityServiceTest {

    @Mock
    CityRepo cityRepo;

    @InjectMocks
    CityService cityService;

    @Test
    void importCityDataThatDoesNotExists() throws IOException {

        // Arrange - Given
        String json = "[{\"name\":\"Venice\",\"local_names\":{\"lt\":\"Venecija\",\"it\":\"Venezia\",\"fi\":\"Venetsia\",\"en\":\"Venice\",\"ro\":\"Veneţia\",\"hr\":\"Venecija\",\"sl\":\"Benetke\",\"ku\":\"Wênîs\",\"lb\":\"Venedeg\",\"es\":\"Venecia\",\"zh\":\"威尼斯\",\"tt\":\"Венеция\",\"fy\":\"Feneesje\",\"ca\":\"Venècia\",\"bg\":\"Венеция\",\"pt\":\"Veneza\",\"ja\":\"ヴェネツィア\",\"la\":\"Venetiae\",\"cs\":\"Benátky\",\"sk\":\"Benátky\",\"sr\":\"Венеција\",\"ve\":\"Venesia\",\"eo\":\"Venecio\",\"pl\":\"Wenecja\",\"sv\":\"Venedig\",\"uk\":\"Венеція\",\"rm\":\"Vnescha\",\"fa\":\"ونیز\",\"is\":\"Feneyjar\",\"ko\":\"베네치아\",\"tr\":\"Venedik\",\"nl\":\"Venetië\",\"fr\":\"Venise\",\"et\":\"Veneetsia\",\"he\":\"ונציה\",\"vi\":\"Vơ-ni-dơ\",\"el\":\"Βενετία\",\"be\":\"Венецыя\",\"da\":\"Venedig\",\"cy\":\"Fenis\",\"de\":\"Venedig\",\"kn\":\"ವೆನಿಸ್\",\"hu\":\"Velence\",\"ar\":\"البندقية\",\"ru\":\"Венеция\"},\"lat\":45.4371908,\"lon\":12.3345898,\"country\":\"IT\",\"state\":\"Veneto\"}]";
        String ip = "212.189.172.164";
        Mockito.when(cityRepo.existsByNameAndCountry("venice","it")).thenReturn(false);
        Mockito.when(cityRepo.save(createCity())).thenReturn(createCity());
        // Act - When
        City testCity = cityService.importCityData(json, ip);
        // Assert - Then
        assertThat(testCity).isEqualTo(createCity());

    }

    @Test
    void importCityDataThatThrowsError() throws IOException {

        // Arrange - Given
        String json = "[{\"name\":\"Venice\",\"local_names\":{\"lt\":\"Venecija\",\"it\":\"Venezia\",\"fi\":\"Venetsia\",\"en\":\"Venice\",\"ro\":\"Veneţia\",\"hr\":\"Venecija\",\"sl\":\"Benetke\",\"ku\":\"Wênîs\",\"lb\":\"Venedeg\",\"es\":\"Venecia\",\"zh\":\"威尼斯\",\"tt\":\"Венеция\",\"fy\":\"Feneesje\",\"ca\":\"Venècia\",\"bg\":\"Венеция\",\"pt\":\"Veneza\",\"ja\":\"ヴェネツィア\",\"la\":\"Venetiae\",\"cs\":\"Benátky\",\"sk\":\"Benátky\",\"sr\":\"Венеција\",\"ve\":\"Venesia\",\"eo\":\"Venecio\",\"pl\":\"Wenecja\",\"sv\":\"Venedig\",\"uk\":\"Венеція\",\"rm\":\"Vnescha\",\"fa\":\"ونیز\",\"is\":\"Feneyjar\",\"ko\":\"베네치아\",\"tr\":\"Venedik\",\"nl\":\"Venetië\",\"fr\":\"Venise\",\"et\":\"Veneetsia\",\"he\":\"ונציה\",\"vi\":\"Vơ-ni-dơ\",\"el\":\"Βενετία\",\"be\":\"Венецыя\",\"da\":\"Venedig\",\"cy\":\"Fenis\",\"de\":\"Venedig\",\"kn\":\"ವೆನಿಸ್\",\"hu\":\"Velence\",\"ar\":\"البندقية\",\"ru\":\"Венеция\"},\"lat\":45.4371908,\"lon\":12.3345898,\"country\":\"IT\",\"state\":\"Veneto\"}]";
        String ip = "212.189.172.164";
        Mockito.when(cityRepo.existsByNameAndCountry("venice","it")).thenReturn(true);
        // Act - When + Assert - Then
        assertThatThrownBy(() -> cityService.importCityData(json, ip)).isInstanceOf(CityAlreadyExistsException.class);

    }

    @Test
    void getCityByNameAndCountry() {
    }

    @Test
    void getCityByName() {
    }

    @Test
    void addCity() {

//        // Arrange - Given
//        City city = cityService.addCity(createCity());
//        // Act - When
//        City testCity = cityService.importCityData("[{\"name\":\"Venice\",\"local_names\":{\"lt\":\"Venecija\",\"it\":\"Venezia\",\"fi\":\"Venetsia\",\"en\":\"Venice\",\"ro\":\"Veneţia\",\"hr\":\"Venecija\",\"sl\":\"Benetke\",\"ku\":\"Wênîs\",\"lb\":\"Venedeg\",\"es\":\"Venecia\",\"zh\":\"威尼斯\",\"tt\":\"Венеция\",\"fy\":\"Feneesje\",\"ca\":\"Venècia\",\"bg\":\"Венеция\",\"pt\":\"Veneza\",\"ja\":\"ヴェネツィア\",\"la\":\"Venetiae\",\"cs\":\"Benátky\",\"sk\":\"Benátky\",\"sr\":\"Венеција\",\"ve\":\"Venesia\",\"eo\":\"Venecio\",\"pl\":\"Wenecja\",\"sv\":\"Venedig\",\"uk\":\"Венеція\",\"rm\":\"Vnescha\",\"fa\":\"ونیز\",\"is\":\"Feneyjar\",\"ko\":\"베네치아\",\"tr\":\"Venedik\",\"nl\":\"Venetië\",\"fr\":\"Venise\",\"et\":\"Veneetsia\",\"he\":\"ונציה\",\"vi\":\"Vơ-ni-dơ\",\"el\":\"Βενετία\",\"be\":\"Венецыя\",\"da\":\"Venedig\",\"cy\":\"Fenis\",\"de\":\"Venedig\",\"kn\":\"ವೆನಿಸ್\",\"hu\":\"Velence\",\"ar\":\"البندقية\",\"ru\":\"Венеция\"},\"lat\":45.4371908,\"lon\":12.3345898,\"country\":\"IT\",\"state\":\"Veneto\"}]", "212.189.172.164");
//        // Assert - Then

    }

    @Test
    void deleteCity() {
    }

    private City createCity() {
        City city = new City();
        city.setIp("212.189.172.164");
        city.setCountry("it");
        city.setName("venice");
        city.setLatitude(BigDecimal.valueOf(45.4371908));
        city.setLongitude(BigDecimal.valueOf(12.3345898));
        return city;
    }
}