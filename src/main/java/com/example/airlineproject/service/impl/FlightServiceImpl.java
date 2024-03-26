package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.repository.FlightRepository;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CompanyService;
import com.example.airlineproject.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final CompanyService companyService;
    private final PlaneRepository planeRepository;

    @Override
    public Flight save(SpringUser springUser, String from, String to, LocalDateTime scheduledTime, LocalDateTime estimatedTime, LocalDateTime arrivalTime, int planeId) {
        User user = springUser.getUser();
        Company company = companyService.findByUser(user);
        if (company != null) {
            Optional<Plane> byId = planeRepository.findById(planeId);
            if (byId.isPresent()) {
                Plane plane = byId.get();
                Flight savedFlight = flightRepository.save(Flight.builder()
                        .from(from)
                        .to(to)
                        .scheduledTime(scheduledTime)
                        .estimatedTime(estimatedTime)
                        .arrivalTime(arrivalTime)
                        .plane(plane)
                        .company(company)
                        .status(Status.ON_TIME)
                        .build());
                log.info("Saved flight: {}", savedFlight);
                return savedFlight;
            } else {
                log.error("Plane with ID {} not found", planeId);
            }
        } else {
            log.error("Company not found for user {}", user.getId());
        }
        return null;
    }


}



