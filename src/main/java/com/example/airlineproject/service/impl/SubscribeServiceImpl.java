package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Subscribe;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.CompanyMapper;
import com.example.airlineproject.repository.SubscribeRepository;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final CompanyMapper companyMapper;
    private final FlightService flightService;
    @Override
    public List<CompanyFewDetailsDto> findCompaniesByUserFromSubscribe(User currentUser) {
        return  companyMapper.companyToCompanyFewDtoList(subscribeRepository.findAllCompaniesByUser(currentUser));
    }




}

