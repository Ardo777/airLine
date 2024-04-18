package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.enums.Status;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T16:03:57+0400",
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

        flight.status( Status.ON_TIME );

        return flight.build();
    }
}
