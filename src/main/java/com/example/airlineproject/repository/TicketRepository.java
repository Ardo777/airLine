package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Booking;
import com.example.airlineproject.entity.BookingType;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByFlightAndSitBelt(Flight flight,String sitBelt);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.flight = :flight")
    int findCountByFlight(@Param("flight") Flight flight);
}
