package com.skycast.service;

import com.skycast.exception.CityAlreadyExistsException;
import com.skycast.exception.CityNotFoundException;
import com.skycast.model.City;
import com.skycast.repo.CityRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class CityServiceTest {

    @Mock
    CityRepo cityRepo;

    @InjectMocks
    CityService cityService;

    @Test
    void importCityData_WhenCityDoesntAlreadyExist_ThenShouldReturnCity() throws IOException {

        // Arrange - Given
        String json = "[{\"name\":\"Venice\",\"local_names\":{\"lt\":\"Venecija\",\"it\":\"Venezia\",\"fi\":\"Venetsia\",\"en\":\"Venice\",\"ro\":\"Veneţia\",\"hr\":\"Venecija\",\"sl\":\"Benetke\",\"ku\":\"Wênîs\",\"lb\":\"Venedeg\",\"es\":\"Venecia\",\"zh\":\"威尼斯\",\"tt\":\"Венеция\",\"fy\":\"Feneesje\",\"ca\":\"Venècia\",\"bg\":\"Венеция\",\"pt\":\"Veneza\",\"ja\":\"ヴェネツィア\",\"la\":\"Venetiae\",\"cs\":\"Benátky\",\"sk\":\"Benátky\",\"sr\":\"Венеција\",\"ve\":\"Venesia\",\"eo\":\"Venecio\",\"pl\":\"Wenecja\",\"sv\":\"Venedig\",\"uk\":\"Венеція\",\"rm\":\"Vnescha\",\"fa\":\"ونیز\",\"is\":\"Feneyjar\",\"ko\":\"베네치아\",\"tr\":\"Venedik\",\"nl\":\"Venetië\",\"fr\":\"Venise\",\"et\":\"Veneetsia\",\"he\":\"ונציה\",\"vi\":\"Vơ-ni-dơ\",\"el\":\"Βενετία\",\"be\":\"Венецыя\",\"da\":\"Venedig\",\"cy\":\"Fenis\",\"de\":\"Venedig\",\"kn\":\"ವೆನಿಸ್\",\"hu\":\"Velence\",\"ar\":\"البندقية\",\"ru\":\"Венеция\"},\"lat\":45.4371908,\"lon\":12.3345898,\"country\":\"IT\",\"state\":\"Veneto\"}]";
        String ip = "212.189.172.164";
        City expectedCity = createCity();
        Mockito.when(cityRepo.existsByNameAndCountry("venice","it")).thenReturn(false);
        Mockito.when(cityRepo.save(expectedCity)).thenReturn(expectedCity);
        // Act - When
        City testCity = cityService.importCityData(json, ip);
        // Assert - Then
        assertThat(testCity).isEqualTo(expectedCity);
    }

    @Test
    void importCityData_WhenCityAlreadyExists_ShouldThrowCityAlreadyExistsException() {

        // Arrange - Given
        String json = "[{\"name\":\"Venice\",\"local_names\":{\"lt\":\"Venecija\",\"it\":\"Venezia\",\"fi\":\"Venetsia\",\"en\":\"Venice\",\"ro\":\"Veneţia\",\"hr\":\"Venecija\",\"sl\":\"Benetke\",\"ku\":\"Wênîs\",\"lb\":\"Venedeg\",\"es\":\"Venecia\",\"zh\":\"威尼斯\",\"tt\":\"Венеция\",\"fy\":\"Feneesje\",\"ca\":\"Venècia\",\"bg\":\"Венеция\",\"pt\":\"Veneza\",\"ja\":\"ヴェネツィア\",\"la\":\"Venetiae\",\"cs\":\"Benátky\",\"sk\":\"Benátky\",\"sr\":\"Венеција\",\"ve\":\"Venesia\",\"eo\":\"Venecio\",\"pl\":\"Wenecja\",\"sv\":\"Venedig\",\"uk\":\"Венеція\",\"rm\":\"Vnescha\",\"fa\":\"ونیز\",\"is\":\"Feneyjar\",\"ko\":\"베네치아\",\"tr\":\"Venedik\",\"nl\":\"Venetië\",\"fr\":\"Venise\",\"et\":\"Veneetsia\",\"he\":\"ונציה\",\"vi\":\"Vơ-ni-dơ\",\"el\":\"Βενετία\",\"be\":\"Венецыя\",\"da\":\"Venedig\",\"cy\":\"Fenis\",\"de\":\"Venedig\",\"kn\":\"ವೆನಿಸ್\",\"hu\":\"Velence\",\"ar\":\"البندقية\",\"ru\":\"Венеция\"},\"lat\":45.4371908,\"lon\":12.3345898,\"country\":\"IT\",\"state\":\"Veneto\"}]";
        String ip = "212.189.172.164";
        Mockito.when(cityRepo.existsByNameAndCountry("venice","it")).thenReturn(true);
        // Act - When + Assert - Then
        assertThatThrownBy(() -> cityService.importCityData(json, ip)).isInstanceOf(CityAlreadyExistsException.class);
    }

    @Test
    public void getCityByNameAndCountry_WhenCityExists_ShouldReturnCity() throws CityNotFoundException {
        String name = "TestCity";
        String country = "TestCountry";
        City expectedCity = createCity();

        when(cityRepo.findByNameAndCountry(name, country)).thenReturn(Optional.of(expectedCity));

        City actualCity = cityService.getCityByNameAndCountry(name, country);

        assertThat(actualCity).isEqualTo(expectedCity);
        verify(cityRepo).findByNameAndCountry(name, country);
    }

    @Test
    public void getCityByNameAndCountry_WhenCityDoesNotExist_ShouldThrowCityNotFoundException() {
        String name = "TestCity";
        String country = "TestCountry";

        when(cityRepo.findByNameAndCountry(name, country)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getCityByNameAndCountry(name, country))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessageContaining("City and country not found");
        verify(cityRepo).findByNameAndCountry(name, country);
    }

    @Test
    public void getCityByName_WhenCityExists_ShouldReturnCity() throws CityNotFoundException {
        String name = "TestCity";
        City expectedCity = createCity();

        when(cityRepo.findByName(name)).thenReturn(Optional.of(expectedCity));

        City actualCity = cityService.getCityByName(name);

        assertThat(actualCity).isEqualTo(expectedCity);
        verify(cityRepo).findByName(name);
    }

    @Test
    public void getCityByName_WhenCityDoesNotExist_ShouldThrowCityNotFoundException() {
        String name = "TestCity";

        when(cityRepo.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getCityByName(name))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessageContaining("City not found");
        verify(cityRepo).findByName(name);
    }

    @Test
    public void addCity_WhenCityDoesNotExist_ShouldReturnSavedCity() throws CityAlreadyExistsException {
        City city = new City();
        city.setName("TestCity");
        city.setCountry("TestCountry");

        when(cityRepo.existsByNameAndCountry(city.getName(), city.getCountry())).thenReturn(false);
        when(cityRepo.save(city)).thenReturn(city);

        City actualCity = cityService.addCity(city);

        assertThat(actualCity).isEqualTo(city);
        verify(cityRepo).existsByNameAndCountry(city.getName(), city.getCountry());
        verify(cityRepo).save(city);
    }

    @Test
    public void addCity_WhenCityExists_ShouldThrowCityAlreadyExistsException() {
        City city = new City();
        city.setName("TestCity");
        city.setCountry("TestCountry");

        when(cityRepo.existsByNameAndCountry(city.getName(), city.getCountry())).thenReturn(true);

        assertThatThrownBy(() -> cityService.addCity(city))
                .isInstanceOf(CityAlreadyExistsException.class)
                .hasMessageContaining("City already exists");
        verify(cityRepo).existsByNameAndCountry(city.getName(), city.getCountry());
    }

    @Test
    public void deleteCity_WhenCityExists_ShouldCallCityRepoDelete() throws CityNotFoundException {
        String name = "TestCity";
        String country = "TestCountry";
        City city = new City();

        when(cityRepo.findByNameAndCountry(name, country)).thenReturn(Optional.of(city));

        cityService.deleteCity(name, country);

        verify(cityRepo).delete(city);
    }

    @Test
    public void deleteCity_WhenCityDoesNotExist_ShouldThrowCityNotFoundException() throws CityNotFoundException {
        String name = "TestCity";
        String country = "TestCountry";

        when(cityRepo.findByNameAndCountry(name, country)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.deleteCity(name, country))
                .isInstanceOf(CityNotFoundException.class);
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