package com.example.airlineproject.dto;

import com.example.airlineproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyFilterDto {
    private int id;
    private String picName;
    private String name;
    private String email;
    private User user;
    private boolean isActive;
}
