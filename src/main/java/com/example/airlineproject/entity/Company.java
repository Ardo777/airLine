package com.example.airlineproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;


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
    private int rating;
    @OneToMany(mappedBy = "company")
    private List<Office> office;
    @OneToMany(mappedBy = "company")
    private List<TeamMember> teamMembers;
    @OneToMany(mappedBy = "company")
    private List<Plane> planes;
    @OneToMany(mappedBy = "company")
    private List<Flight> flights;
}
