package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findAllByCompanyAndStatusNot(Company company, Status status);

    Optional<Flight> findByIdAndCompany(int id, Company company);
}
