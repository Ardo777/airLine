package com.example.airlineproject.dto;
import com.example.airlineproject.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlaneUpdateDto {

    private int id;
    private String model;
    private String planePic;
    private double maxBaggage;
    private Company company;
    private int countEconomy;
    private int countBusiness;
}
