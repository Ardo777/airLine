package com.example.airlineproject.entity;

import com.example.airlineproject.entity.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@Builder
public class User{

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
    @Value("${user.defaultRole:USER}")
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
    @Column(name = "date_Birthday")
    private LocalDate birthday;


    public User(String alice, String smith, String mail, String somepassword, boolean b) {
    }
}
