package com.example.airlineproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    private int id;
    @NotBlank(message = "From location must not be blank")
    private String from;

    @NotBlank(message = "To location must not be blank")
    private String to;

    @Positive(message = "Economy price must be greater than zero")
    private double economyPrice;

    @Positive(message = "Business price must be greater than zero")
    private double businessPrice;

    @NotNull(message = "Scheduled time must not be null")
    private LocalDateTime scheduledTime;

    @NotNull(message = "Estimated time must not be null")
    private LocalDateTime estimatedTime;

    @NotNull(message = "Arrival time must not be null")
    private LocalDateTime arrivalTime;
    private CompanyFewDetailsDto company;
    private PlaneDto plane;
}
