package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.entity.Plane;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaneMapper {
    List<PlaneDto> map(List<Plane> planes);

    PlaneDto map(Plane plane);

    Plane map(PlaneUpdateDto planeUpdateDto);
}
