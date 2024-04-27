package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.entity.Company;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-25T12:23:15+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyFewDetailsDto companyToCompanyFewDto(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyFewDetailsDto.CompanyFewDetailsDtoBuilder companyFewDetailsDto = CompanyFewDetailsDto.builder();

        companyFewDetailsDto.id( company.getId() );
        companyFewDetailsDto.name( company.getName() );
        companyFewDetailsDto.picName( company.getPicName() );

        return companyFewDetailsDto.build();
    }
}
