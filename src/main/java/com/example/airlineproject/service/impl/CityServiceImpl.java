package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.City;
import com.example.airlineproject.repository.CityRepository;
import com.example.airlineproject.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<City> getCitiesByCountry(int countryId) {
        return cityRepository.findByCountry_Id(countryId);
    }
}

