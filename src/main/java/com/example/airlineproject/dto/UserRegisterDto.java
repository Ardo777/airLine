package com.example.airlineproject.dto;

import com.example.airlineproject.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String confirmPassword;
    private String verificationCode;
    private String picName;
    private UserRole userRole;
    private LocalDate birthday;
}
