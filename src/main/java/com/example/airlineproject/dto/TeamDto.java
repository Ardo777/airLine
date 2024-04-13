package com.example.airlineproject.dto;

import com.example.airlineproject.entity.enums.Profession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TeamDto {

    private String name;
    private String surname;
    private Profession profession;

}
