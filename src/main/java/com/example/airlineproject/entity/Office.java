package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "office")
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String country;
    private String city;
    private String phone;
    @DateTimeFormat(pattern = "HH:mm")
    private Date workStartTime;
    @DateTimeFormat(pattern = "HH:mm")
    private Date workEndTime;
    private String street;
    @ManyToOne
    private Company company;
}
