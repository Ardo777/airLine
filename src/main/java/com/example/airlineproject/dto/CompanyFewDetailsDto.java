package com.example.airlineproject.dto;

import com.example.airlineproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyFewDetailsDto {
    private int id;
    private String name;
    private String picName;
    private User user;
}