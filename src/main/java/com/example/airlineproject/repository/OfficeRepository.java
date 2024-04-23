package com.example.airlineproject.repository;

import com.example.airlineproject.entity.City;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Country;
import com.example.airlineproject.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {

    boolean existsByCountryAndCityAndStreet(Country country, City city, String street);

    Office findByCompany(Company company);

}
