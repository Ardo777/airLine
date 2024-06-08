package com.example.airlineproject.service;

import com.example.airlineproject.dto.ResponseBusinessBookingDto;
import com.example.airlineproject.entity.BookingType;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Ticket;
import com.example.airlineproject.entity.User;

import java.util.List;

public interface BookingService {
    List<ResponseBusinessBookingDto> findBookingFlightsByFlightIdAndType(int id, BookingType bookingType);

    void saveBooking(Ticket ticket, User user, BookingType bookingType, Flight flight);


}
