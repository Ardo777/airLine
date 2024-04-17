package com.example.airlineproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDto {
    private String password;
    private String newPassword;
    private String confirmNewPassword;
}
