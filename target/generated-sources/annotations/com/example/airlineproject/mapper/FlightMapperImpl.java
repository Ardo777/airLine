package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.ChangeFlightDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightResponseDto;
import com.example.airlineproject.dto.FlightsResponseDto;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.enums.Status;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class FlightMapperImpl implements FlightMapper {

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

        flightResponseDto.id( flight.getId() );
        flightResponseDto.from( flight.getFrom() );
        flightResponseDto.to( flight.getTo() );
        flightResponseDto.scheduledTime( flight.getScheduledTime() );
        flightResponseDto.estimatedTime( flight.getEstimatedTime() );
        flightResponseDto.arrivalTime( flight.getArrivalTime() );
        flightResponseDto.status( flight.getStatus() );
        flightResponseDto.plane( flight.getPlane() );
        flightResponseDto.company( flight.getCompany() );

        return flightResponseDto.build();
    }

    @Override
    public Flight map(ChangeFlightDto changeFlightDto) {
        if ( changeFlightDto == null ) {
            return null;
        }

        Flight.FlightBuilder flight = Flight.builder();

        flight.id( changeFlightDto.getId() );
        flight.from( changeFlightDto.getFrom() );
        flight.to( changeFlightDto.getTo() );
        flight.scheduledTime( changeFlightDto.getScheduledTime() );
        flight.estimatedTime( changeFlightDto.getEstimatedTime() );
        flight.arrivalTime( changeFlightDto.getArrivalTime() );

        return flight.build();
    }

    @Override
    public List<FlightsResponseDto> map(List<Flight> flights) {
        if ( flights == null ) {
            return null;
        }

        List<FlightsResponseDto> list = new ArrayList<FlightsResponseDto>( flights.size() );
        for ( Flight flight : flights ) {
            list.add( flightToFlightsResponseDto( flight ) );
        }

        return list;
    }

    protected FlightsResponseDto flightToFlightsResponseDto(Flight flight) {
        if ( flight == null ) {
            return null;
        }

        FlightsResponseDto.FlightsResponseDtoBuilder flightsResponseDto = FlightsResponseDto.builder();

        flightsResponseDto.id( flight.getId() );
        flightsResponseDto.from( flight.getFrom() );
        flightsResponseDto.to( flight.getTo() );
        flightsResponseDto.scheduledTime( flight.getScheduledTime() );
        flightsResponseDto.estimatedTime( flight.getEstimatedTime() );
        flightsResponseDto.arrivalTime( flight.getArrivalTime() );
        flightsResponseDto.status( flight.getStatus() );

        return flightsResponseDto.build();
    }
}
