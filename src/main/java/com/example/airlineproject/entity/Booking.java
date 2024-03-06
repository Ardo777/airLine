package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Ticket ticket;
    @ManyToOne
    private User user;
    private String comment;
    //*booking_dat(a) must replace like this <booking_date>*
    @Column(name = "comment_data")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bookingDate;


}
