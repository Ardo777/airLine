package com.example.airlineproject.service;

import com.example.airlineproject.entity.City;

import java.util.List;

public interface CityService {

    List<City> getCitiesByCountry(int countryId);

}
