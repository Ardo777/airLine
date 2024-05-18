package com.example.airlineproject.repository;

import com.example.airlineproject.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findByCountry_Id(int countryId);
}
