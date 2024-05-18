package com.example.airlineproject.service;

import com.example.airlineproject.dto.*;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.security.SpringUser;

import java.util.List;

public interface FlightService {

    Flight save(FlightDto flightDto, SpringUser springUser, int planeId);

    List<FlightsResponseDto> findExistingFlights(Company company, Status status);

    FlightResponseDto findCompanyFlight(int flight,Company company);

    void changeFLight(ChangeFlightDto changeFlightDto, int planeId, Company company);

    List<FlightsListResponseDto> findFirst10Flights();
}
