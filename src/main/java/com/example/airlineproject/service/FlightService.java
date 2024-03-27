package com.example.airlineproject.service;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.security.SpringUser;

public interface FlightService {

    Flight save(FlightDto flightDto, SpringUser springUser, int planeId);

}
