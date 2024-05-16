package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.*;
import com.example.airlineproject.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", imports = LocalDate.class)
public interface FlightMapper {

    @Mapping(target = "status", constant = "ON_TIME")
    @Mapping(target = "plane", ignore = true)
    @Mapping(target = "company", ignore = true)
    Flight map(FlightDto flightDto);
    FlightResponseDto map(Flight flight);

    Flight map(ChangeFlightDto changeFlightDto);

    List<FlightsResponseDto> map(List<Flight> flights);

    List<FlightsListResponseDto> mapToFlightsListResponseDto(List<Flight> flights);

}
