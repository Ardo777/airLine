package com.example.airlineproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company")
@Builder
@EqualsAndHashCode(exclude = {"user", "office", "teamMembers", "planes", "flights", "certificates"})
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "name can't be empty")
    private String name;
    @NotEmpty(message = "email can't be empty")
    private String email;
    @NotBlank(message = "picture can't be empty")
    private String picName;
    @NotBlank(message = "picture can't be empty")
    private String certificatePic;
    private boolean isActive;
    @OneToOne
    @JsonIgnore
    private User user;
    private int rating;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Office> office;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<TeamMember> teamMembers;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Plane> planes;
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", picName='" + picName + '\'' +
                ", isActive=" + isActive +
                ", user=" + user +
                ", rating=" + rating +
                ", office=" + office +
                ", flights=" + flights +
                '}';
    }

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Flight> flights;
}
