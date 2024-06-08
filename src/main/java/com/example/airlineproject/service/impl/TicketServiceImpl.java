package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.CreateTicketDto;
import com.example.airlineproject.entity.BookingType;
import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.Ticket;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.FlightMapper;
import com.example.airlineproject.mapper.TicketMapper;
import com.example.airlineproject.repository.TicketRepository;
import com.example.airlineproject.service.BookingService;
import com.example.airlineproject.service.CardService;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final FlightService flightService;
    private final FlightMapper flightMapper;
    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final CardService cardService;
    private final BookingService bookingService;
    @Override
    public void createTicketAndBooking(CreateTicketDto createTicketDto, int flightId, User currentUser) {
        Flight flight = flightMapper.mapToFlight(flightService.findById(flightId));
        int countByFlight = ticketRepository.findCountByFlight(flight);
        if (countByFlight >= flight.getPlane().getCountEconomy() + flight.getPlane().getCountBusiness()){
            throw new RuntimeException();//TicketNotAvailableException
        }

        BookingType business ;
        if (createTicketDto.getSitBelt() == null || createTicketDto.getSitBelt().isEmpty()){
            createTicketDto.setPrice(flight.getEconomyPrice());
            business = BookingType.ECONOMY;
        }else {
            createTicketDto.setPrice(flight.getBusinessPrice());
            business = BookingType.BUSINESS;
        }
        boolean transfer = cardService.transfer(createTicketDto.getPrice(), flight.getCompany().getUser(), currentUser);
        if (!transfer){
            throw new RuntimeException(); //balanceIsLessException;
        }

        createTicketDto.setFlight(flightMapper.flightToFlightDto(flight));
        Ticket ticket = ticketMapper.mapToTicket(createTicketDto);
        ticketRepository.save(ticket);
        bookingService.saveBooking(ticket,currentUser,business,flight);

    }




}
