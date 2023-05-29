package com.skycast.repo;

import com.skycast.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherRepo extends JpaRepository<Weather, Long>  {

    Optional<Weather> findByDt(Long dt);
    boolean existsByDt(Long dt);

}
