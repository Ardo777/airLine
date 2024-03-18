package com.example.airlineproject.entity;

import com.example.airlineproject.annotation.UniqueEmail;
import com.example.airlineproject.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "please write your name, name can't be empty")
    private String name;
    @NotEmpty(message = "please write your surname, surname can't be empty")
    private String surname;
    @UniqueEmail(message = "this user already exist")
    private String email;
    @Size(min = 6, message = "Password cannot be shorter than 6 characters")
    @NotEmpty(message = "please write your password, password can't be empty")
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private boolean isActive;
    private String verificationCode;
    private String picName;
    @Transient
    private String confirmPassword;
    @ManyToOne
    private Company company;

}
