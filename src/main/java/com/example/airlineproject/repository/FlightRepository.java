package com.example.airlineproject.repository;

import com.example.airlineproject.dto.FlightsListResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    List<Flight> findAllByCompanyAndStatusNot(Company company, Status status);

    List<Flight> findAllByStatusNot(Status status);

    Optional<Flight> findByIdAndCompany(int id, Company company);

    @Query("SELECT f FROM Flight f WHERE f.scheduledTime > ?1 ORDER BY f.scheduledTime ASC LIMIT 10")
    List<Flight> findClosestToCurrentTime(LocalDateTime currentTime);
}


