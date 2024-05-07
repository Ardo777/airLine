package com.example.airlineproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaneAddDto {

    private String model;
    private double maxBaggage;
    private int countBusiness;
    private int countEconomy;
    private int countRow;


}
