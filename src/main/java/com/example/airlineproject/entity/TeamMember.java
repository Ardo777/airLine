package com.example.airlineproject.entity;

import com.example.airlineproject.entity.enums.Profession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team_member")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "name can't be empty")
    private String name;
    @NotEmpty(message = "surname can't be empty")
    private String surname;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    @ManyToOne
    private Company company;
    @Column(name = "is_active")
    private boolean active;
}
