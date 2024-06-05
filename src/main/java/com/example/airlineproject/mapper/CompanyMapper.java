package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Subscribe;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyFewDetailsDto companyToCompanyFewDto(Company company);
    List<CompanyFewDetailsDto> companyToCompanyFewDtoList(List<Company> companies);
    List<Company> CompanyFewDetailsDtoListToCompany(List<CompanyFewDetailsDto> CompanyFewDetailsDtoList);
}
