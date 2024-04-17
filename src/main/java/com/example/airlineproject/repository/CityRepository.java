package com.example.airlineproject.repository;

import com.example.airlineproject.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {

}
