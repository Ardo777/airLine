package com.example.airlineproject.dto;

import com.example.airlineproject.entity.enums.Profession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamMemberChangeDto {

    private int id;
    private String name;
    private String surname;
    private Profession profession;

}
