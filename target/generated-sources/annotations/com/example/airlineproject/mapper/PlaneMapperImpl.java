package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.PlanesResponseDto;
import com.example.airlineproject.entity.Plane;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T21:55:25+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class PlaneMapperImpl implements PlaneMapper {

    @Override
    public List<PlanesResponseDto> map(List<Plane> planes) {
        if ( planes == null ) {
            return null;
        }

        List<PlanesResponseDto> list = new ArrayList<PlanesResponseDto>( planes.size() );
        for ( Plane plane : planes ) {
            list.add( planeToPlanesResponseDto( plane ) );
        }

        return list;
    }

    protected PlanesResponseDto planeToPlanesResponseDto(Plane plane) {
        if ( plane == null ) {
            return null;
        }

        PlanesResponseDto.PlanesResponseDtoBuilder planesResponseDto = PlanesResponseDto.builder();

        planesResponseDto.id( plane.getId() );
        planesResponseDto.model( plane.getModel() );
        planesResponseDto.countEconomy( plane.getCountEconomy() );
        planesResponseDto.countBusiness( plane.getCountBusiness() );

        return planesResponseDto.build();
    }
}
