package com.example.airlineproject.controller;

import com.example.airlineproject.dto.CreateTicketDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightFilterDto;
import com.example.airlineproject.entity.BookingType;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.BookingService;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/flight")
@Slf4j
public class FlightController {

    private final FlightService flightService;
    private final BookingService bookingService;
    private final TicketService ticketService;
    @GetMapping("/list")
    public String flightListPage(ModelMap modelMap) {
        modelMap.addAttribute("flights", flightService.findExistingFlights());
        return "flight-listing";
    }

    @GetMapping("/list/filter")
    public String flightsFilter(@ModelAttribute FlightFilterDto flightFilterDto, ModelMap modelMap){
        List<FlightDto> allFlightsByFilter = flightService.getAllFlightsByFilter(flightFilterDto);
        if (allFlightsByFilter.isEmpty()){
            return "redirect:/flight/list";
        }
        modelMap.addAttribute("flights", allFlightsByFilter);
        return "flight-listing";
    }

    @GetMapping("/booking/{flightId}")
    public String flightBooking(@PathVariable("flightId") int id, ModelMap modelMap){
        modelMap.addAttribute("bookings", bookingService.findBookingFlightsByFlightIdAndType(id, BookingType.BUSINESS));
        modelMap.addAttribute("flight", flightService.findById(id));
        return "flight-booking";
    }
    @PostMapping("/booking/ticket")
    public String createTicketAndBooking(@ModelAttribute CreateTicketDto createTicketDto, @RequestParam("flightId") int fightId, @AuthenticationPrincipal SpringUser springUser){
        ticketService.createTicketAndBooking(createTicketDto,fightId,springUser.getUser());
        return "redirect:/flight/booking/"+fightId;
    };

}
