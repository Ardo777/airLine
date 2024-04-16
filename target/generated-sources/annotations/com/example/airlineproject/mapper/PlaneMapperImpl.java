package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.PlaneRequestDto;
import com.example.airlineproject.dto.PlaneResponseDto;
import com.example.airlineproject.entity.Plane;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T08:27:34+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class PlaneMapperImpl implements PlaneMapper {

    @Override
    public List<PlaneResponseDto> map(List<Plane> planes) {
        if ( planes == null ) {
            return null;
        }

        List<PlaneResponseDto> list = new ArrayList<PlaneResponseDto>( planes.size() );
        for ( Plane plane : planes ) {
            list.add( map( plane ) );
        }

        return list;
    }

    @Override
    public PlaneResponseDto map(Plane plane) {
        if ( plane == null ) {
            return null;
        }

        PlaneResponseDto.PlaneResponseDtoBuilder planeResponseDto = PlaneResponseDto.builder();

        planeResponseDto.id( plane.getId() );
        planeResponseDto.model( plane.getModel() );
        planeResponseDto.planePic( plane.getPlanePic() );
        planeResponseDto.maxBaggage( plane.getMaxBaggage() );
        planeResponseDto.countEconomy( plane.getCountEconomy() );
        planeResponseDto.countBusiness( plane.getCountBusiness() );

        return planeResponseDto.build();
    }

    @Override
    public Plane map(PlaneRequestDto planeRequestDto) {
        if ( planeRequestDto == null ) {
            return null;
        }

        Plane.PlaneBuilder plane = Plane.builder();

        plane.id( planeRequestDto.getId() );
        plane.model( planeRequestDto.getModel() );
        plane.planePic( planeRequestDto.getPlanePic() );
        plane.company( planeRequestDto.getCompany() );
        plane.maxBaggage( planeRequestDto.getMaxBaggage() );
        plane.countEconomy( planeRequestDto.getCountEconomy() );
        plane.countBusiness( planeRequestDto.getCountBusiness() );

        return plane.build();
    }
}
