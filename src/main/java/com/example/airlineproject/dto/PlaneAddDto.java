package com.example.airlineproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaneAddDto {

    @NotBlank(message = "Model is mandatory")
    private String model;

    @Positive(message = "Max baggage must be positive")
    private double maxBaggage;

    private MultipartFile picture;

    @Min(value = 0, message = "Count of business class seats must be zero or positive")
    private int countBusiness;

    @Min(value = 0, message = "Count of economy class seats must be zero or positive")
    private int countEconomy;

    @Min(value = 0, message = "Count of rows must be zero or positive")
    private int countRow;

}
