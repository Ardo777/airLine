package com.example.airlineproject.entity;

import com.example.airlineproject.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String from;
    private String to;
    private Date scheduledTime;
    private Date estimatedTime;
    private Date arrivalTime;
    private Status status;
    @ManyToOne
    private Plane plane;
    @ManyToOne
    private Company company;

}
