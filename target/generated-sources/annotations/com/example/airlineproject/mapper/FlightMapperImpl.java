package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightResponseDto;
import com.example.airlineproject.dto.FlightsListResponseDto;
import com.example.airlineproject.dto.UpdateFlightDto;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.enums.Status;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T15:45:47+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class FlightMapperImpl implements FlightMapper {

    @Autowired
    private PlaneMapper planeMapper;
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Flight map(FlightDto flightDto) {
        if ( flightDto == null ) {
            return null;
        }

        Flight.FlightBuilder flight = Flight.builder();

        flight.from( flightDto.getFrom() );
        flight.to( flightDto.getTo() );
        flight.scheduledTime( flightDto.getScheduledTime() );
        flight.estimatedTime( flightDto.getEstimatedTime() );
        flight.arrivalTime( flightDto.getArrivalTime() );
        flight.economyPrice( flightDto.getEconomyPrice() );
        flight.businessPrice( flightDto.getBusinessPrice() );

        flight.status( Status.ON_TIME );

        return flight.build();
    }

    @Override
    public FlightResponseDto map(Flight flight) {
        if ( flight == null ) {
            return null;
        }

        FlightResponseDto.FlightResponseDtoBuilder flightResponseDto = FlightResponseDto.builder();

        flightResponseDto.from( flight.getFrom() );
        flightResponseDto.to( flight.getTo() );
        flightResponseDto.scheduledTime( flight.getScheduledTime() );
        flightResponseDto.estimatedTime( flight.getEstimatedTime() );
        flightResponseDto.arrivalTime( flight.getArrivalTime() );
        flightResponseDto.status( flight.getStatus() );
        flightResponseDto.company( flight.getCompany() );
        flightResponseDto.economyPrice( flight.getEconomyPrice() );
        flightResponseDto.businessPrice( flight.getBusinessPrice() );

        return flightResponseDto.build();
    }

    @Override
    public List<FlightDto> flightsToFlightDtoList(List<Flight> flights) {
        if ( flights == null ) {
            return null;
        }

        List<FlightDto> list = new ArrayList<FlightDto>( flights.size() );
        for ( Flight flight : flights ) {
            list.add( flightToFlightDto( flight ) );
        }

        return list;
    }

    @Override
    public Flight map(UpdateFlightDto updateFlightDto) {
        if ( updateFlightDto == null ) {
            return null;
        }

        Flight.FlightBuilder flight = Flight.builder();

        flight.id( updateFlightDto.getId() );
        flight.from( updateFlightDto.getFrom() );
        flight.to( updateFlightDto.getTo() );
        flight.scheduledTime( updateFlightDto.getScheduledTime() );
        flight.estimatedTime( updateFlightDto.getEstimatedTime() );
        flight.arrivalTime( updateFlightDto.getArrivalTime() );

        return flight.build();
    }

    @Override
    public List<FlightResponseDto> flightsToFlightResponseDtoList(List<Flight> flights) {
        if ( flights == null ) {
            return null;
        }

        List<FlightResponseDto> list = new ArrayList<FlightResponseDto>( flights.size() );
        for ( Flight flight : flights ) {
            list.add( map( flight ) );
        }

        return list;
    }

    @Override
    public List<FlightsListResponseDto> mapToFlightsListResponseDto(List<Flight> flights) {
        if ( flights == null ) {
            return null;
        }

        List<FlightsListResponseDto> list = new ArrayList<FlightsListResponseDto>( flights.size() );
        for ( Flight flight : flights ) {
            list.add( flightToFlightsListResponseDto( flight ) );
        }

        return list;
    }

    protected FlightDto flightToFlightDto(Flight flight) {
        if ( flight == null ) {
            return null;
        }

        FlightDto.FlightDtoBuilder flightDto = FlightDto.builder();

        flightDto.from( flight.getFrom() );
        flightDto.to( flight.getTo() );
        flightDto.economyPrice( flight.getEconomyPrice() );
        flightDto.businessPrice( flight.getBusinessPrice() );
        flightDto.scheduledTime( flight.getScheduledTime() );
        flightDto.estimatedTime( flight.getEstimatedTime() );
        flightDto.arrivalTime( flight.getArrivalTime() );
        flightDto.company( companyMapper.companyToCompanyFewDto( flight.getCompany() ) );
        flightDto.plane( planeMapper.map( flight.getPlane() ) );

        return flightDto.build();
    }

    protected FlightsListResponseDto flightToFlightsListResponseDto(Flight flight) {
        if ( flight == null ) {
            return null;
        }

        FlightsListResponseDto.FlightsListResponseDtoBuilder flightsListResponseDto = FlightsListResponseDto.builder();

        flightsListResponseDto.id( flight.getId() );
        flightsListResponseDto.from( flight.getFrom() );
        flightsListResponseDto.to( flight.getTo() );
        flightsListResponseDto.scheduledTime( flight.getScheduledTime() );
        flightsListResponseDto.estimatedTime( flight.getEstimatedTime() );
        flightsListResponseDto.arrivalTime( flight.getArrivalTime() );
        flightsListResponseDto.economyPrice( flight.getEconomyPrice() );
        flightsListResponseDto.businessPrice( flight.getBusinessPrice() );
        flightsListResponseDto.status( flight.getStatus() );
        flightsListResponseDto.plane( flight.getPlane() );
        flightsListResponseDto.company( flight.getCompany() );

        return flightsListResponseDto.build();
    }
}
