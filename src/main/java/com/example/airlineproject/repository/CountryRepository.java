package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {

Country findByName(String name);

}
