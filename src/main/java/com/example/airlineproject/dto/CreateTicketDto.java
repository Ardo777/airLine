package com.example.airlineproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTicketDto {
    private String sitBelt;
    private double baggageKg;
    private FlightDto flight;
    private double price;
    private String name;
    private String lastName;
    private String email;
}
