package com.example.airlineproject.dto;

import com.example.airlineproject.entity.City;
import com.example.airlineproject.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfficeChangeDto {
    private int id;
    private Country country;
    private City city;
    private String phone;
    @DateTimeFormat(pattern = "HH:mm")
    private Date workStartTime;
    @DateTimeFormat(pattern = "HH:mm")
    private Date workEndTime;
    private String street;
}
