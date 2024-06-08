package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Country;
import com.example.airlineproject.repository.CountryRepository;
import com.example.airlineproject.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

}
