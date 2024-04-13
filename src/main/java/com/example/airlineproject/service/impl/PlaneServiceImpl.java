package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.PlanesResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.mapper.PlaneMapper;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.service.PlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final PlaneMapper planeMapper;
    @Override
    public List<PlanesResponseDto> allPlanesOfTheCompany(Company company) {
        return planeMapper.map(planeRepository.findAllByCompany(company));
    }
}
