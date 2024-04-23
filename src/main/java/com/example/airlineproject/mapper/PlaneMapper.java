package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.PlaneAddDto;
import com.example.airlineproject.entity.Plane;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaneMapper {

    Plane mapToPlane(PlaneAddDto planeAddDto);

}
