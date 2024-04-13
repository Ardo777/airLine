package com.example.airlineproject.service;

import com.example.airlineproject.dto.PlanesResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Plane;

import java.util.List;

public interface PlaneService {
    List<PlanesResponseDto> allPlanesOfTheCompany(Company company);
}
