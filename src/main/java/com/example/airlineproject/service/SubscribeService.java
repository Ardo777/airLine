package com.example.airlineproject.service;

import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.dto.FlightResponseDto;
import com.example.airlineproject.entity.User;

import java.util.List;

public interface SubscribeService {
    List<CompanyFewDetailsDto> findCompaniesByUserFromSubscribe(User currentUser);

}
