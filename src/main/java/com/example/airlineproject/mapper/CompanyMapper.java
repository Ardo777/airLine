package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.entity.Company;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyFewDetailsDto companyToCompanyFewDto(Company company);
}
