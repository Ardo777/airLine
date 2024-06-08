package com.example.airlineproject.service;

import com.example.airlineproject.dto.CreateTicketDto;
import com.example.airlineproject.entity.User;

public interface TicketService {
    void createTicketAndBooking(CreateTicketDto createTicketDto, int flightId, User user);
}
