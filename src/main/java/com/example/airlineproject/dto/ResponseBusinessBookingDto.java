package com.example.airlineproject.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBusinessBookingDto {
    private TicketResponseFewDetailsDto ticket;
    private double price;
}
