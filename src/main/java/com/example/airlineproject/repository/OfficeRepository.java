package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {
    boolean existsByCountryAndCityAndStreet(String country, String city, String street);

}
