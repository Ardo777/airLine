package com.example.airlineproject.entity;

import com.example.airlineproject.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "`from`")
    private String from;
    @Column(name = "`to`")
    private String to;
    private LocalDateTime scheduledTime;
    private LocalDateTime estimatedTime;
    private LocalDateTime arrivalTime;
    private double economyPrice;
    private double businessPrice;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    private Plane plane;
    @ManyToOne
    private Company company;

    @Override
    public String toString() {
        return "Flight{" +
                "status=" + status +
                ", businessPrice=" + businessPrice +
                ", economyPrice=" + economyPrice +
                ", arrivalTime=" + arrivalTime +
                ", estimatedTime=" + estimatedTime +
                ", scheduledTime=" + scheduledTime +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", id=" + id +
                '}';
    }
}
