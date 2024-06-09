package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.entity.Company;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-09T11:10:32+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
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
        companyFewDetailsDto.user( company.getUser() );

        return companyFewDetailsDto.build();
    }

    @Override
    public List<CompanyFewDetailsDto> companyToCompanyFewDtoList(List<Company> companies) {
        if ( companies == null ) {
            return null;
        }

        List<CompanyFewDetailsDto> list = new ArrayList<CompanyFewDetailsDto>( companies.size() );
        for ( Company company : companies ) {
            list.add( companyToCompanyFewDto( company ) );
        }

        return list;
    }

    @Override
    public List<Company> CompanyFewDetailsDtoListToCompany(List<CompanyFewDetailsDto> CompanyFewDetailsDtoList) {
        if ( CompanyFewDetailsDtoList == null ) {
            return null;
        }

        List<Company> list = new ArrayList<Company>( CompanyFewDetailsDtoList.size() );
        for ( CompanyFewDetailsDto companyFewDetailsDto : CompanyFewDetailsDtoList ) {
            list.add( companyFewDetailsDtoToCompany( companyFewDetailsDto ) );
        }

        return list;
    }

    protected Company companyFewDetailsDtoToCompany(CompanyFewDetailsDto companyFewDetailsDto) {
        if ( companyFewDetailsDto == null ) {
            return null;
        }

        Company.CompanyBuilder company = Company.builder();

        company.id( companyFewDetailsDto.getId() );
        company.name( companyFewDetailsDto.getName() );
        company.picName( companyFewDetailsDto.getPicName() );
        company.user( companyFewDetailsDto.getUser() );

        return company.build();
    }
}
