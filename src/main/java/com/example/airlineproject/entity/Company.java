package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
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
    // here must be name  validation
    @NotEmpty(message = "name can't be empty")
    private String name;
    @NotEmpty(message = "email can't be empty")
    private String email;
    @NotEmpty(message = "picture can't be empty")
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
