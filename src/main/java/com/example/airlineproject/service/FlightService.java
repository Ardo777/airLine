package com.example.airlineproject.service;

import com.example.airlineproject.dto.*;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.security.SpringUser;

import java.util.List;

public interface FlightService {

    Flight save(FlightDto flightDto, SpringUser springUser, int planeId);

    List<FlightsResponseDto> findExistingFlights(Company company, Status status);

    FlightResponseDto findCompanyFlight(int flight, Company company);

    void changeFLight(UpdateFlightDto updateFlightDto, int planeId, Company company);

    List<FlightsListResponseDto> findFirst10Flights();

    List<FlightDto> getAllFlightsByFilter(FlightFilterDto flightFilterDto);

    List<FlightDto> findExistingFlights();

    List<FlightResponseDto> flightsOfSubscriptions(List<Company> companies);

    FlightDto findById(int id);
}
