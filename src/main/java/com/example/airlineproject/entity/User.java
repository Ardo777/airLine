package com.example.airlineproject.entity;

import com.example.airlineproject.entity.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "please write your name, name can't be empty")
    private String name;
    @NotEmpty(message = "please write your surname, surname can't be empty")
    private String surname;
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Invalid email address")
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
    @OneToOne
    @ToString.Exclude
    private Company company;
    private LocalDate dateBirthday;

    public User(int i, String alice, String smith, String mail, String password, UserRole userRole, boolean b, String verificationCode, String picName, Object o, Object o1) {

    }
}
