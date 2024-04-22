package com.example.airlineproject.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeFlightDto {
    private int id;
    @NotEmpty(message = "from should not be empty")
    private String from;
    @NotEmpty(message = "to should not be empty")
    private String to;
    @NotEmpty(message = "scheduled time should not be empty")
    private LocalDateTime scheduledTime;
    @NotEmpty(message = "estimated time Time should not be empty")
    private LocalDateTime estimatedTime;
    @NotEmpty(message = "arrival time should not be empty")
    private LocalDateTime arrivalTime;
}
