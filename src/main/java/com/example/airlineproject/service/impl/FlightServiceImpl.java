package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.*;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.*;
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
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {
    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;
    private final CompanyService companyService;
    private final PlaneRepository planeRepository;
    private final EntityManager entityManager;

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
        return List.of();
    }

//    @Override
//    public List<FlightsResponseDto> findExistingFlights(Company company, Status status) {
//        log.debug("finding existing flights");
//        return flightMapper.flightsToFlightResponseDtoList(flightRepository.findAllByCompanyAndStatusNot(company, status));
//    }

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
    public void changeFLight(UpdateFlightDto updateFlightDto, int planeId, Company company) {
        log.debug("Finding airplane with ID {} for company {}", planeId, company);
        Optional<Plane> plane = planeRepository.findByIdAndCompany(planeId, company);
        log.debug("Finding existing flight with ID {} for company {}", updateFlightDto.getId(), company);
        Optional<Flight> existingFlight = flightRepository.findByIdAndCompany(updateFlightDto.getId(), company);
        if (existingFlight.isEmpty()) {
            log.error("Flight not found with ID {} and  company {}", updateFlightDto.getId(), company);
            throw new FlightNotFoundException();
        }
        if (plane.isEmpty()) {
            log.error("Plane not found with ID {} and company {}", planeId, company);
            throw new PlaneNotFoundException();
        }
        Flight modifiedFlight = flightMapper.map(updateFlightDto);
        modifiedFlight.setPlane(plane.get());
        modifiedFlight.setCompany(company);
        log.debug("status of the flight  will change");
        modifiedFlight.setStatus(getFlightStatusByTime(modifiedFlight, existingFlight.get()));
        log.debug("Saving modified flight");
        flightRepository.save(modifiedFlight);
    }

    public List<FlightsListResponseDto> findFirst10Flights() {
        List<Flight> flightsList = flightRepository.findClosestToCurrentTime(LocalDateTime.now());
        List<FlightsListResponseDto> flightsListResponseDto = flightMapper.mapToFlightsListResponseDto(flightsList);
        log.info("First 10 flights already taken");
        return flightsListResponseDto;
    }


    @Override
    public List<FlightDto> getAllFlightsByFilter(FlightFilterDto flightFilterDto) {
        JPAQuery<Flight> query = new JPAQuery<>(entityManager);
        QFlight qFlight = QFlight.flight;
        JPAQueryBase from = query.from(qFlight);
        if (flightFilterDto.getTo() != null && !flightFilterDto.getTo().isEmpty()){
            from.where(qFlight.to.contains(flightFilterDto.getTo()));
        }
        if (flightFilterDto.getScheduledTime() != null && flightFilterDto.getScheduledTime().isAfter(LocalDateTime.now())){
            from.where(qFlight.scheduledTime.eq(flightFilterDto.getScheduledTime()));
        }
        if (flightFilterDto.getFrom() != null && !flightFilterDto.getFrom().isEmpty()){
            from.where(qFlight.from.contains(flightFilterDto.getFrom()));
        }
        if (flightFilterDto.getMinimumPrice() != null && flightFilterDto.getMaximumPrice() != null){
            from.where(qFlight.businessPrice.between(flightFilterDto.getMinimumPrice(),flightFilterDto.getMaximumPrice()).or(qFlight.economyPrice.between(flightFilterDto.getMinimumPrice(),flightFilterDto.getMaximumPrice())));
        } else if (flightFilterDto.getMinimumPrice() != null) {
            from.where(qFlight.economyPrice.goe(flightFilterDto.getMinimumPrice()).or(qFlight.businessPrice.goe(flightFilterDto.getMinimumPrice())));
        } else if (flightFilterDto.getMaximumPrice() != null) {
            from.where(qFlight.economyPrice.loe(flightFilterDto.getMaximumPrice()).or(qFlight.businessPrice.loe(flightFilterDto.getMaximumPrice())));
        }
        return flightMapper.flightsToFlightDtoList(query.fetch());
    }

    @Override
    public List<FlightDto> findExistingFlights() {
        return flightMapper.flightsToFlightDtoList(flightRepository.findAllByStatusNot(Status.ARRIVED));
    }

    @Override
    public List<FlightResponseDto> flightsOfSubscriptions(List<Company> companies) {
        return flightMapper.flightsToFlightResponseDtoList(flightRepository.findAllByCompanyInAndStatusNot(companies,Status.ARRIVED));
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
            log.error("Flight time exception: Scheduled Time of modified flight ({}) is before current time or Estimated Time is before Scheduled Time or Arrival Time is before Estimated Time or Scheduled Time of existing flight ({}) is equal to current time", modifiedFlight.getId(), existingFlight.getId());
            throw new FlightTimeException();
        }
        if (modifiedFlight.getScheduledTime().isBefore(existingFlight.getScheduledTime())) {
            log.debug("Modified flight {} scheduled earlier than existing flight {}", modifiedFlight.getId(), existingFlight.getId());
            return Status.EARLIER;
        } else if (modifiedFlight.getScheduledTime().isAfter(existingFlight.getScheduledTime())) {
            log.debug("Modified flight {} scheduled after existing flight {}", modifiedFlight.getId(), existingFlight.getId());
            return Status.PREMATURE;
        }
        log.debug("Modified flight {} scheduled at the same time as existing flight {}", modifiedFlight.getId(), existingFlight.getId());
        return Status.ON_TIME;
    }
}



