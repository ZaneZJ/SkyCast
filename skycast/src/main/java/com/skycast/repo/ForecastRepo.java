package com.skycast.repo;


//
//import com.skycast.model.Forecast;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Repository
//public interface ForecastRepo extends JpaRepository<Forecast, Long> {
//
////    Optional<Forecast> findByDtTxt(LocalDateTime dtTxt);
////    boolean existsByDtTxt(LocalDateTime dtTxt);
//
//}

import org.springframework.data.jpa.repository.JpaRepository;
import com.skycast.model.Forecast;
import org.springframework.stereotype.Repository;

@Repository
public interface ForecastRepo extends JpaRepository<Forecast, Integer> {
    // repository definition here



}