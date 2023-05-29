package com.skycast.repo;

import com.skycast.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CityRepo extends JpaRepository<City, Long> {

    Optional<City> findByNameAndCountry(String name, String country);
    Optional<City> findByName(String name);
    boolean existsByNameAndCountry(String name, String country);
    boolean existsByName(String name);

}
