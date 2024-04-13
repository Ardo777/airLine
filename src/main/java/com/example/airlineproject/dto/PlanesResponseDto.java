package com.example.airlineproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanesResponseDto {
    private int id;
    private String model;
    private int countEconomy;
    private int countBusiness;
}
