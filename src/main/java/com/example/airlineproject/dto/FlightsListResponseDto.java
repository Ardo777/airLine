package com.example.airlineproject.dto;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightsListResponseDto {

    private int id;
    private String from;
    private String to;
    private LocalDateTime scheduledTime;
    private LocalDateTime estimatedTime;
    private LocalDateTime arrivalTime;
    private double economyPrice;
    private double businessPrice;
    private Status status;
    private Plane plane;
    private Company company;
}
