package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "star_rating")
@Builder
public class StarRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int rating;
    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;
    @JoinColumn(name = "company_id")
    @ManyToOne
    private Company company;
}
