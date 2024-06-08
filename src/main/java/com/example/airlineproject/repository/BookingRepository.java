package com.example.airlineproject.repository;

import com.example.airlineproject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByFlight_IdAndBookingType(int flightId, BookingType bookingType);

}
