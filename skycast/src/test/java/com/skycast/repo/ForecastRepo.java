package com.skycast.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import com.skycast.model.Forecast;

public interface ForecastRepo extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByDtTxt(LocalDateTime dtTxt);
    boolean existsByDtTxt(LocalDateTime dtTxt);

}
