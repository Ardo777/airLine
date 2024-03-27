package com.example.airlineproject.controller;

import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    public String addFlight(@ModelAttribute FlightDto flightDto, @RequestParam("plane") int planeId, @AuthenticationPrincipal SpringUser springUser) {
        flightService.save(flightDto, springUser, planeId);
        return "redirect:/manager";
    }


}
