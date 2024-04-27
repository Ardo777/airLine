package com.example.airlineproject.controller;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.FlightFilterDto;
import com.example.airlineproject.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/flight")
@Slf4j
public class FlightController {

    private final FlightService flightService;
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
}
