package com.example.airlineproject.controller;


import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.service.ManagerService;
import com.example.airlineproject.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class ManagerController {
    private final ManagerService managerService;
    private final PlaneRepository planeRepository;
    private final FlightService flightService;
    private final TeamService teamService;


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
        return "/manager/moreDetails";
    }


    @GetMapping("/addFlight")
    public String addFlightPage(ModelMap modelMap) {
        modelMap.addAttribute("planes", planeRepository.findAll());
        log.info("List of planes sent to HTML");
        return "manager/moreDetails";
    }

    @PostMapping("/addFlight")
    public String addFlight(@ModelAttribute FlightDto flightDto, @RequestParam("plane") int planeId, @AuthenticationPrincipal SpringUser springUser) {
        flightService.save(flightDto, springUser, planeId);
        return "redirect:/manager";
    }

    @GetMapping("/addFlight")
    public String addFlight() {
        return "/manager/flight";
    }


    @PostMapping("/addAirPlane")
    public String addAirPlane(@RequestParam("model") String model,
                              @RequestParam("maxBaggage") double maxBaggage,
                              @RequestParam("countBusiness") int countBusiness,
                              @RequestParam("countEconomy") int countEconomy,
                              @AuthenticationPrincipal SpringUser springUser,
                              @RequestParam("picture") MultipartFile multipartFile) throws IOException {

        log.debug("Model: {}", model);
        log.debug("Max Baggage: {}", maxBaggage);
        log.debug("Count Business Places: {}", countBusiness);
        log.debug("Count Economy Places: {}", countEconomy);
        log.debug("User: {}", springUser.getUsername());
        Plane plane = managerService.createPlane(model, maxBaggage, countBusiness,countEconomy, multipartFile);
        Boolean isPlaneExist = managerService.isPlaneExist(plane,springUser.getUser());
        plane.setCompany(springUser.getUser().getCompany());
        if (isPlaneExist) {
            String planeErrorMsg = "A plane with these parameters was previously added to your company";
            log.warn(planeErrorMsg);
            return "redirect:/manager/moreDetails?planeErrorMsg=" + planeErrorMsg;
        }
        managerService.saveAirPlane(plane, multipartFile);
        String planeSuccessMsg = "Airplane added successfully";
        log.info(planeSuccessMsg);
        return "redirect:/manager/moreDetails?planeSuccessMsg=" + planeSuccessMsg;
    }


    @PostMapping("/addOffice")
    public String addOffice(@ModelAttribute Office office,
                            @AuthenticationPrincipal SpringUser springUser) {
        Boolean isOfficeExist = managerService.isOfficeExist(office);
        office.setCompany(springUser.getUser().getCompany());
        if (isOfficeExist) {
            String officeErrorMsg = "An office already exists at this address";
            log.warn("Office already exists at this address");
            return "redirect:/manager/moreDetails?officeErrorMsg=" + officeErrorMsg;
        }
        managerService.saveOffice(office, springUser.getUser());
        String officeSuccessMsg = "Office added successfully";
        log.info("Office added successfully");
        return "redirect:/manager/moreDetails?officeSuccessMsg=" + officeSuccessMsg;
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
}
