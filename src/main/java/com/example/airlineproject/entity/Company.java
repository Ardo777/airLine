package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company")
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "name can't be empty")
    private String name;
    @NotEmpty(message = "email can't be empty")
    private String email;
    @NotEmpty(message = "picture can't be empty")
    private String picName;
    private boolean isActive;
    @OneToOne
    private User user;
}
