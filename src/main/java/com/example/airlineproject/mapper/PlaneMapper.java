package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.PlaneAddDto;
import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.entity.Plane;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaneMapper {

    PlaneDto map(Plane plane);

    Plane mapToPlane(PlaneAddDto planeAddDto);

    List<PlaneDto> planeToPlaneDto(List<Plane> planes);

    Plane map(PlaneUpdateDto planeUpdateDto);


}
