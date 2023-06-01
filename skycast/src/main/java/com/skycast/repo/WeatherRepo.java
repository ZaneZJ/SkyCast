package com.skycast.repo;

import com.skycast.model.City;
import com.skycast.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepo extends JpaRepository<Weather, Long>  {

    Optional<Weather> findByDtAndCity(String dateTime, City city);
    boolean existsByDtAndCity(String dateTime, City city);

}
