package com.example.airlineproject.dto;

import com.example.airlineproject.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private int id;
    private String name;
    private String surname;
    private String email;
    private UserRole role;
    private String picName;
}
