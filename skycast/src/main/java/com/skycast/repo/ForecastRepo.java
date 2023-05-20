package com.skycast.repo;

import com.skycast.model.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ForecastRepo extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByDtTxt(LocalDateTime dtTxt);
    boolean existsByDtTxt(LocalDateTime dtTxt);

}
