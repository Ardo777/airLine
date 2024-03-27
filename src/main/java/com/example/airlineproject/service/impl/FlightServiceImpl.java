package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.FlightMapper;
import com.example.airlineproject.repository.FlightRepository;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CompanyService;
import com.example.airlineproject.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {
    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;
    private final CompanyService companyService;
    private final PlaneRepository planeRepository;

    @Override
    public Flight save(FlightDto flightDto, SpringUser springUser, int planeId) {
        User user = springUser.getUser();
        log.info("Received request to save flight for user: {}", user.getUsername());
        Company company = companyService.findByUser(user);
        if (company != null) {
            log.debug("Found company for user: {}", user.getUsername());
            Optional<Plane> byId = planeRepository.findById(planeId);
            if (byId.isPresent()) {
                log.debug("Found plane with ID: {}", planeId);
                Plane plane = byId.get();
                Flight flight = flightMapper.map(flightDto);
                flight.setPlane(plane);
                flight.setCompany(company);
                log.info("Saving flight: {}", flight);
                flightRepository.save(flight);
                log.info("Flight saved successfully");
                return flight;
            } else {
                log.error("Plane with ID {} not found", planeId);
            }
        } else {
            log.error("Company not found for user {}", user.getId());
        }
        return null;
    }


}



