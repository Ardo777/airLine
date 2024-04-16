package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.PlaneRequestDto;
import com.example.airlineproject.dto.PlaneResponseDto;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Plane;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaneMapper {
    List<PlaneResponseDto> map(List<Plane> planes);

    PlaneResponseDto map(Plane plane);

    Plane map(PlaneRequestDto planeRequestDto);
}
