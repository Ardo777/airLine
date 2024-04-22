package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.ChangeFlightDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightResponseDto;
import com.example.airlineproject.dto.FlightsResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.exception.FlightNotFoundException;
import com.example.airlineproject.exception.FlightTimeException;
import com.example.airlineproject.exception.PlaneNotFoundException;
import com.example.airlineproject.mapper.FlightMapper;
import com.example.airlineproject.repository.FlightRepository;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CompanyService;
import com.example.airlineproject.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    @Override
    public List<FlightsResponseDto> findExistingFlights(Company company, Status status) {
        log.debug("finding existing flights");
        return flightMapper.map(flightRepository.findAllByCompanyAndStatusNot(company, status));
    }

    @Override
    public FlightResponseDto findCompanyFlight(int flightId, Company company) {
        log.debug("Finding flight with ID {} and company {}", flightId, company.getName());
        Optional<Flight> flight = flightRepository.findByIdAndCompany(flightId, company);
        if (flight.isEmpty()) {
            log.error("Flight not found with ID {} and  company {}", flightId, company);
            throw new FlightNotFoundException();
        }
        log.debug("flight will be return {}", flight.get());
        return flightMapper.map(flight.get());
    }

    @Override
    public void changeFLight(ChangeFlightDto changeFlightDto, int planeId, Company company) {
        // this method of the repository will find airplane of the company
        log.debug("Finding airplane with ID {} for company {}", planeId, company);
        Optional<Plane> plane = planeRepository.findByIdAndCompany(planeId, company);
        // this method of the repository will find existing flight of the company
        log.debug("Finding existing flight with ID {} for company {}", changeFlightDto.getId(), company);
        Optional<Flight> existingFlight = flightRepository.findByIdAndCompany(changeFlightDto.getId(), company);
        if (existingFlight.isEmpty()) {
            // when manager change flight id from inspect then findByIdAndCompany method will find existing flight by id and company
           //when this method <findByIdAndCompany> doesn't find that flight with id and company So, this flight either does not exist, or it belongs to another company that has an id of the flight
            log.error("Flight not found with ID {} and  company {}", changeFlightDto.getId(), company);
            throw new FlightNotFoundException();
        }
        if (plane.isEmpty()) {
            //when this method <findByIdAndCompany> doesn't find that plane with plane id and company So, this airplane either does not exist, or it belongs to another company that has an id of the plane
            log.error("Plane not found with ID {} and company {}", planeId, company);
            throw new PlaneNotFoundException();
        }
        Flight modifiedFlight = flightMapper.map(changeFlightDto);
        modifiedFlight.setPlane(plane.get());
        modifiedFlight.setCompany(company);
        //this method will check flight time and return status of the flight
        log.debug("status of the flight  will change");
        modifiedFlight.setStatus(getFlightStatusByTime(modifiedFlight, existingFlight.get()));
        log.debug("Saving modified flight");
        flightRepository.save(modifiedFlight);
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




    private Status getFlightStatusByTime(Flight modifiedFlight, Flight existingFlight) {
        if (modifiedFlight.getScheduledTime().isBefore(LocalDateTime.now())
                || modifiedFlight.getEstimatedTime().isBefore(modifiedFlight.getScheduledTime())
                || modifiedFlight.getArrivalTime().isBefore(modifiedFlight.getEstimatedTime())
                || existingFlight.getScheduledTime().isEqual(LocalDateTime.now())
        ) {
            //1)The scheduled time should not be earlier than the current time
            //2)The Estimated Time should not be earlier than the Scheduled Time
            //3)The Arrival Time should not be earlier than the Estimated Time
            //4)The Scheduled Time should not be equal than the current time
            log.error("Flight time exception: Scheduled Time of modified flight ({}) is before current time or Estimated Time is before Scheduled Time or Arrival Time is before Estimated Time or Scheduled Time of existing flight ({}) is equal to current time", modifiedFlight.getId(), existingFlight.getId());
            throw new FlightTimeException();
        }
        //if modified Flight time earlier than existing flight time, so status will return earlier
        if (modifiedFlight.getScheduledTime().isBefore(existingFlight.getScheduledTime())) {
            log.debug("Modified flight {} scheduled earlier than existing flight {}", modifiedFlight.getId(), existingFlight.getId());
            return Status.EARLIER;
        } else if (modifiedFlight.getScheduledTime().isAfter(existingFlight.getScheduledTime())) {
            log.debug("Modified flight {} scheduled after existing flight {}", modifiedFlight.getId(), existingFlight.getId());
            //if modified Flight time after than existing flight time, so status will return premature
            return Status.PREMATURE;
        }
        log.debug("Modified flight {} scheduled at the same time as existing flight {}", modifiedFlight.getId(), existingFlight.getId());
        return Status.ON_TIME;
    }
}



