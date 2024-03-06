package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String picName;
    @OneToOne
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    List<Comment> comments;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    List<Office> offices;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    List<Plane> planes;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    List<TeamMember> teamMembers;
}
