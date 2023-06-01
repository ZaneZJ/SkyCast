package com.skycast.repo;

import com.skycast.model.City;
import com.skycast.model.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForecastRepo extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByDtTxtAndCity(String dtTxt, City city);
    boolean existsByDtTxtAndCity(String dtTxt, City city);

}