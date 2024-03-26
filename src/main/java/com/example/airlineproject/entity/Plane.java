package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return Double.compare(maxBaggage, plane.maxBaggage) == 0 && maxPassengers == plane.maxPassengers && Objects.equals(model, plane.model) && Objects.equals(company, plane.company);
    }

    @Override
    public String toString() {
        return "Plane{" +
                "model='" + model + '\'' +
                ", company=" + company +
                ", maxBaggage=" + maxBaggage +
                ", maxPassengers=" + maxPassengers +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, company, maxBaggage, maxPassengers);
    }
}
