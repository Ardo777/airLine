package com.example.airlineproject.controller;

import com.example.airlineproject.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

//    @GetMapping("/")

}
