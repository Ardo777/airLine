package com.example.airlineproject.controller;


import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.City;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CityService;
import com.example.airlineproject.service.CountryService;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

    private final PlaneRepository planeRepository;
    private final FlightService flightService;
    private final TeamService teamService;
    private final CountryService countryService;
    private final CityService cityService;


    @GetMapping
    public String managerPage() {
        return "/manager/index";
    }

    @GetMapping("/moreDetails")
    public String moreDetails(@RequestParam(value = "planeSuccessMsg", required = false) String planeSuccessMsg,
                              @RequestParam(value = "planeErrorMsg", required = false) String planeErrorMsg,
                              @RequestParam(value = "officeSuccessMsg", required = false) String officeSuccessMsg,
                              @RequestParam(value = "officeErrorMsg", required = false) String officeErrorMsg,
                              ModelMap modelMap) {
        if (planeSuccessMsg != null) {
            modelMap.put("planeSuccessMsg", planeSuccessMsg);
            log.info("Success message: {}", planeSuccessMsg);
        }
        if (officeSuccessMsg != null) {
            modelMap.put("officeSuccessMsg", officeSuccessMsg);
            log.info("Office Success Msg message: {}", officeSuccessMsg);
        }
        if (planeErrorMsg != null) {
            modelMap.put("planeErrorMsg", planeErrorMsg);
            log.error("Error message: {}", planeErrorMsg);
        }
        if (officeErrorMsg != null) {
            modelMap.put("officeErrorMsg", officeErrorMsg);
            log.error("Error message: {}", officeErrorMsg);
        }
        modelMap.addAttribute("countries", countryService.getAllCountries());
        log.info("List of countries sent to HTML");
        return "/manager/moreDetails";
    }


    @GetMapping("/addFlight")
    public String addFlightPage(ModelMap modelMap) {
        modelMap.addAttribute("planes", planeRepository.findAll());
        log.info("List of planes sent to HTML");
        return "/manager/moreDetails";
    }

    @PostMapping("/addFlight")
    public String addFlight(@ModelAttribute FlightDto flightDto, @RequestParam("plane") int planeId, @AuthenticationPrincipal SpringUser springUser) {
        flightService.save(flightDto, springUser, planeId);
        return "redirect:/manager/moreDetails";
    }


    @PostMapping("/addTeam")
    public String addFlight(@ModelAttribute TeamDto teamDto, @AuthenticationPrincipal SpringUser springUser) {
        log.info("Attempting to add team with data: {}", teamDto);
        TeamMember savedTeamMember = teamService.save(teamDto, springUser);
        if (savedTeamMember != null) {
            log.info("Team member added successfully with ID: {}", savedTeamMember.getId());
            return "redirect:/manager";
        } else {
            return "redirect:/manager/moreDetails";
        }
    }

    @GetMapping("/cities")
    public String fetchCities(@RequestParam("countryId") int countryId, ModelMap modelMap) {
        log.info("Fetching cities77777777777777");
        //        modelMap.addAttribute("cities", cities);
        //        log.info( modelMap.toString());
          return "hello";
    }
}
