package com.example.airlineproject.controller;

import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
    private final PlaneRepository planeRepository;
    private final FlightService flightService;

    @GetMapping("/manager")
    public String managerPage() {
        return "/manager/index";
    }

    @GetMapping("/manager/moreDetails")
    public String moreDetails() {
        return "/manager/moreDetails";
    }

    @GetMapping("/manager/addFlight")
    public String addFlightPage(ModelMap modelMap) {
        modelMap.addAttribute("planes", planeRepository.findAll());
        log.info("List of planes sent to HTML");
        return "manager/moreDetails";
    }

    @PostMapping("/manager/addFlight")
    public String addFlight(@RequestParam("from") String from, @RequestParam("to") String to,
                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam("scheduledTime") LocalDateTime scheduledTime,
                            @RequestParam("estimatedTime") LocalDateTime estimatedTime, @RequestParam("arrivalTime") LocalDateTime arrivalTime,
                            @RequestParam("plane") int planeId, @AuthenticationPrincipal SpringUser springUser) {
        log.info("Received request to add a new flight");
        Flight flight = flightService.save(springUser, from, to, scheduledTime, estimatedTime, arrivalTime, planeId);
        if (flight == null) {
            return "redirect:/manager";
        }
        return "redirect:/manager";
    }


}
