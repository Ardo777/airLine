package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    @OneToOne
    private Country country;
    @OneToOne
    private City city;
    private String phone;
    @DateTimeFormat(pattern = "HH:mm")
    private Date workStartTime;
    @DateTimeFormat(pattern = "HH:mm")
    private Date workEndTime;
    private String street;
    @ManyToOne
    @ToString.Exclude
    private Company company;
}
