package com.example.airlineproject.repository;

import com.example.airlineproject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {

    boolean existsByCountryAndCityAndStreet(Country country, City city, String street);

    Office findByCompany(Company company);

    List<Office> findAllByCompany(Company company);

}
