package com.example.airlineproject.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightFilterDto {
    private String from;
    private String to;
    private LocalDateTime scheduledTime;
    private Double minimumPrice;
    private Double maximumPrice;
}
