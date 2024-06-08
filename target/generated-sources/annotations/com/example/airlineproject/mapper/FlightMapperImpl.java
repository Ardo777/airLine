package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightResponseDto;
import com.example.airlineproject.dto.FlightsListResponseDto;
import com.example.airlineproject.dto.FlightsResponseDto;
import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.UpdateFlightDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.enums.Status;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T22:19:16+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
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

        flight.id( flightDto.getId() );
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
    public Flight mapToFlight(FlightDto flightDto) {
        if ( flightDto == null ) {
            return null;
        }

        Flight.FlightBuilder flight = Flight.builder();

        flight.id( flightDto.getId() );
        flight.from( flightDto.getFrom() );
        flight.to( flightDto.getTo() );
        flight.scheduledTime( flightDto.getScheduledTime() );
        flight.estimatedTime( flightDto.getEstimatedTime() );
        flight.arrivalTime( flightDto.getArrivalTime() );
        flight.economyPrice( flightDto.getEconomyPrice() );
        flight.businessPrice( flightDto.getBusinessPrice() );
        flight.plane( planeDtoToPlane( flightDto.getPlane() ) );
        flight.company( companyFewDetailsDtoToCompany( flightDto.getCompany() ) );

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
    public FlightDto flightToFlightDto(Flight flight) {
        if ( flight == null ) {
            return null;
        }

        FlightDto.FlightDtoBuilder flightDto = FlightDto.builder();

        flightDto.id( flight.getId() );
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
    public List<FlightsResponseDto> flightsToFlightsResponseDtoList(List<Flight> flights) {
        if ( flights == null ) {
            return null;
        }

        List<FlightsResponseDto> list = new ArrayList<FlightsResponseDto>( flights.size() );
        for ( Flight flight : flights ) {
            list.add( flightToFlightsResponseDto( flight ) );
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

    protected Plane planeDtoToPlane(PlaneDto planeDto) {
        if ( planeDto == null ) {
            return null;
        }

        Plane.PlaneBuilder plane = Plane.builder();

        plane.id( planeDto.getId() );
        plane.model( planeDto.getModel() );
        plane.planePic( planeDto.getPlanePic() );
        plane.maxBaggage( planeDto.getMaxBaggage() );
        plane.countEconomy( planeDto.getCountEconomy() );
        plane.countRow( (int) planeDto.getCountRow() );
        plane.countBusiness( planeDto.getCountBusiness() );

        return plane.build();
    }

    protected Company companyFewDetailsDtoToCompany(CompanyFewDetailsDto companyFewDetailsDto) {
        if ( companyFewDetailsDto == null ) {
            return null;
        }

        Company.CompanyBuilder company = Company.builder();

        company.id( companyFewDetailsDto.getId() );
        company.name( companyFewDetailsDto.getName() );
        company.picName( companyFewDetailsDto.getPicName() );
        company.user( companyFewDetailsDto.getUser() );

        return company.build();
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
