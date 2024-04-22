package com.example.airlineproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlaneDto {

    private int id;
    private String model;
    private String planePic;
    private double maxBaggage;
    private int countEconomy;
    private int countBusiness;

}
