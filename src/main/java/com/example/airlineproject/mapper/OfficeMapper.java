package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.OfficeChangeDto;
import com.example.airlineproject.entity.Office;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfficeMapper {

    @Mapping(source = "workStartTime", target = "workStartTime", dateFormat = "HH:mm")
    @Mapping(source = "workEndTime", target = "workEndTime", dateFormat = "HH:mm")
    Office mapToOffice(OfficeChangeDto officeChangeDto);

}
