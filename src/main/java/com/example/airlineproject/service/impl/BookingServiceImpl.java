package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.ResponseBusinessBookingDto;
import com.example.airlineproject.entity.*;
import com.example.airlineproject.mapper.BookMapper;
import com.example.airlineproject.repository.BookingRepository;
import com.example.airlineproject.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final BookMapper bookMapper;

    @Override
    public List<ResponseBusinessBookingDto> findBookingFlightsByFlightIdAndType(int id, BookingType bookingType) {
        return bookMapper.mapToResponseBusinessBookDtoList(bookingRepository.findAllByFlight_IdAndBookingType(id, bookingType));
    }

    @Override
    public void saveBooking(Ticket ticket, User user, BookingType bookingType, Flight flight) {
        bookingRepository.save(Booking.builder()
                .ticket(ticket)
                .user(user)
                .bookingDate(new Date())
                .bookingType(bookingType)
                .price(ticket.getPrice())
                .flight(flight)
                .build());
    }

}
