package com.example.airlineproject.service;

import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.security.SpringUser;

import java.time.LocalDateTime;

public interface FlightService {

    Flight save(SpringUser springUser, String from, String to, LocalDateTime scheduledTime, LocalDateTime estimatedTime, LocalDateTime arrivalTime, int planeId);

}
