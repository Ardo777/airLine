package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = LocalDate.class)
public interface FlightMapper {

    @Mapping(target = "status", constant = "ON_TIME")
    @Mapping(target = "plane", ignore = true)
    @Mapping(target = "company", ignore = true)
    Flight map(FlightDto flightDto);

}
