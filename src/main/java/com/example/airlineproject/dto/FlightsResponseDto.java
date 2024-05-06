package com.example.airlineproject.dto;

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
public class FlightsResponseDto {
    private int id;
    private String from;
    private String to;
    private LocalDateTime scheduledTime;
    private LocalDateTime estimatedTime;
    private LocalDateTime arrivalTime;
    private Status status;
}
