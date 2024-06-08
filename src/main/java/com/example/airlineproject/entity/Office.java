package com.example.airlineproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "office")
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @NotNull(message = "Country must not be null")
    private Country country;

    @OneToOne
    @NotNull(message = "City must not be null")
    private City city;

    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone must be a valid phone number")
    private String phone;

    @NotNull(message = "Work start time must not be null")
    @DateTimeFormat(pattern = "HH:mm")
    private Date workStartTime;

    @NotNull(message = "Work end time must not be null")
    @DateTimeFormat(pattern = "HH:mm")
    private Date workEndTime;

    @NotBlank(message = "Street must not be blank")
    private String street;

    @ManyToOne
    @NotNull(message = "Company must not be null")
    @ToString.Exclude
    private Company company;
}
