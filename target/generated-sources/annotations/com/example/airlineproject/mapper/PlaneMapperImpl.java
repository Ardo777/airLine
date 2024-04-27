package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.entity.Plane;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-27T13:47:10+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class PlaneMapperImpl implements PlaneMapper {

    @Override
    public PlaneDto map(Plane plane) {
        if ( plane == null ) {
            return null;
        }

        PlaneDto.PlaneDtoBuilder planeDto = PlaneDto.builder();

        planeDto.id( plane.getId() );
        planeDto.model( plane.getModel() );
        planeDto.planePic( plane.getPlanePic() );
        planeDto.maxBaggage( plane.getMaxBaggage() );
        planeDto.countEconomy( plane.getCountEconomy() );
        planeDto.countBusiness( plane.getCountBusiness() );

        return planeDto.build();
    }

    @Override
    public List<PlaneDto> planeToPlaneDto(List<Plane> planes) {
        if ( planes == null ) {
            return null;
        }

        List<PlaneDto> list = new ArrayList<PlaneDto>( planes.size() );
        for ( Plane plane : planes ) {
            list.add( map( plane ) );
        }

        return list;
    }

    @Override
    public Plane map(PlaneUpdateDto planeUpdateDto) {
        if ( planeUpdateDto == null ) {
            return null;
        }

        Plane.PlaneBuilder plane = Plane.builder();

        plane.id( planeUpdateDto.getId() );
        plane.model( planeUpdateDto.getModel() );
        plane.planePic( planeUpdateDto.getPlanePic() );
        plane.company( planeUpdateDto.getCompany() );
        plane.maxBaggage( planeUpdateDto.getMaxBaggage() );
        plane.countEconomy( planeUpdateDto.getCountEconomy() );
        plane.countBusiness( planeUpdateDto.getCountBusiness() );

        return plane.build();
    }
}
