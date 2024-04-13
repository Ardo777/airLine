package com.example.airlineproject.controller;


import com.example.airlineproject.dto.ChangeFlightDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.service.ManagerService;
import com.example.airlineproject.service.PlaneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final PlaneService planeService;

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
        Plane plane = managerService.createPlane(model, maxBaggage, countBusiness, countEconomy, multipartFile);
        Boolean isPlaneExist = managerService.isPlaneExist(plane, springUser.getUser());
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
                            @AuthenticationPrincipal SpringUser springUser
    ) {

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

    @GetMapping("/flights")
    public String FlightPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        //this service method will find all flights of the company and will put into model map
        modelMap.addAttribute("flights", flightService.findExistingFlights(springUser.getUser().getCompany(), Status.ARRIVED));
        return "manager/flights";
    }


    @GetMapping("/change/flight/{flightId}")
    public String ChangeFlightPage(@PathVariable("flightId") int flightId, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        //this service method will find something flight which chose manager for change the flight.
        modelMap.addAttribute("flight", flightService.findCompanyFlight(flightId, springUser.getUser().getCompany()));
        //this service method will find all plane of the company and put into model map because manager must choose plane for flight
        modelMap.addAttribute("planes", planeService.allPlanesOfTheCompany(springUser.getUser().getCompany()));
        return "manager/changeFlight";
    }

    @PostMapping("/change/flight")
    public String ChangeFlight(@AuthenticationPrincipal SpringUser springUser,
                               @RequestParam("plane") int planeId,
                               @ModelAttribute("changeFlightDto") ChangeFlightDto changeFlightDto
                               ) {
        //this service method will change that flight.
        //I send changeFlightDto and company each other because manager can enter into the <inspect> and change that flight id and receive another flight of other company
        flightService.changeFLight(changeFlightDto, planeId, springUser.getUser().getCompany());
        return "redirect:/manager/flights";
    }
}
