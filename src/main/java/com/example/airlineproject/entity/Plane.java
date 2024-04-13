package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "plane")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String model;
    private String planePic;
    @ManyToOne
    private Company company;
    private double maxBaggage;
    @Column(name = "count_Economy")
    private int countEconomy;
    @Column(name = "count_Business")
    private int countBusiness;

}
