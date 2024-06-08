package com.example.airlineproject.dto;

import com.example.airlineproject.entity.enums.Profession;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeamDto {

    @NotEmpty(message = "name can't be empty")
    private String name;
    @NotEmpty(message = "surname can't be empty")
    private String surname;
    private Profession profession;

}
