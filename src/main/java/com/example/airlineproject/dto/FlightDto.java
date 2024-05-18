package com.example.airlineproject.dto;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDto {
    private String from;
    private String to;
    private double economyPrice;
    private double businessPrice;
    private LocalDateTime scheduledTime;
    private LocalDateTime estimatedTime;
    private LocalDateTime arrivalTime;
    private CompanyFewDetailsDto company;
    private PlaneDto plane;
}
