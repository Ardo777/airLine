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
        log.info("Received request to save flight for user: {}", user.getEmail());
        Company company = findCompanyByUser(user);
        if (company != null) {
            Plane plane = findPlaneById(planeId);
            if (plane != null) {
                if (isValidPrices(flightDto)) {
                    Flight flight = createFlight(flightDto, plane, company);
                    if (flight != null) {
                        log.info("Saving flight: {}", flight);
                        flightRepository.save(flight);
                        log.info("Flight saved successfully");
                        return flight;
                    }
                } else {
                    log.error("Invalid price entered for economy or business class");
                    return null;
                }
            } else {
                log.error("Plane with ID {} not found", planeId);
                return null;
            }
        } else {
            log.error("Company not found for user {}", user.getId());
        }
        return null;
    }

    private Company findCompanyByUser(User user) {
        log.debug("Searching for company for user: {}", user.getEmail());
        return companyService.findByUser(user);
    }

    private Plane findPlaneById(int planeId) {
        log.debug("Searching for plane with ID: {}", planeId);
        Optional<Plane> optionalPlane = planeRepository.findById(planeId);
        return optionalPlane.orElse(null);
    }

    private boolean isValidPrices(FlightDto flightDto) {
        return isValidPrice(flightDto.getEconomyPrice()) && isValidPrice(flightDto.getBusinessPrice());
    }

    private boolean isValidPrice(Double price) {
        return price != null && price > 0;
    }

    private Flight createFlight(FlightDto flightDto, Plane plane, Company company) {
        Flight flight = flightMapper.map(flightDto);
        flight.setPlane(plane);
        flight.setCompany(company);
        return flight;
    }



}



