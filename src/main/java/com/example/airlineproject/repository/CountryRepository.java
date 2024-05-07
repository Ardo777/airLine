package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

Country findByName(String name);

}
