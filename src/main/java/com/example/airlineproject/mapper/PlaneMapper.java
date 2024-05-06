package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.PlaneAddDto;
import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightResponseDto;
import com.example.airlineproject.dto.FlightsResponseDto;
import com.example.airlineproject.dto.PlanesResponseDto;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.service.PlaneService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaneMapper {

    Plane mapToPlane(PlaneAddDto planeAddDto);
    List<PlaneDto> map(List<Plane> planes);

    PlaneDto map(Plane plane);

    Plane map(PlaneUpdateDto planeUpdateDto);

    List<PlanesResponseDto> map(List<Plane> planes);

}
