package com.example.airlineproject.entity;

import com.example.airlineproject.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    private String email;

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
