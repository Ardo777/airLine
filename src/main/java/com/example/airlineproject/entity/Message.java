package com.example.airlineproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "from_n")
    private User from;
    @ManyToOne
    @JoinColumn(name = "to_n")
    private User to;
    @NotEmpty(message = "write something, message can't be empty")
    private String message;
    private boolean seen;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date messageDate;
}
