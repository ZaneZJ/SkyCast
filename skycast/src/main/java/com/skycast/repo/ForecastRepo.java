package com.skycast.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skycast.model.Forecast;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ForecastRepo extends JpaRepository<Forecast, Long> {

    // repository definition

        Optional<Forecast> findByDtTxt(LocalDateTime dtTxt);
        boolean existsByDtTxt(LocalDateTime dtTxt);

}