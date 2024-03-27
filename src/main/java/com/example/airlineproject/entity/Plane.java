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
    @Column(name = "max_Passengers")
    private int maxPassengers;

}
