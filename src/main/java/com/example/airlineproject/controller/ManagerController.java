package com.example.airlineproject.controller;


import com.example.airlineproject.dto.UpdateFlightDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CountryService;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.service.PlaneService;
import com.example.airlineproject.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

    private final PlaneRepository planeRepository;
    private final FlightService flightService;
    private final PlaneService planeService;
    private final TeamService teamService;
    private final CountryService countryService;
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
                              @AuthenticationPrincipal SpringUser springUser,
                              ModelMap modelMap) {
        if (planeSuccessMsg != null) {
            modelMap.put("planeSuccessMsg", planeSuccessMsg);
            log.info("Success message: {}", planeSuccessMsg);
        }
        if (validateMsgFlight != null) {
            modelMap.put("validateMsgFlight", validateMsgFlight);
            log.info("Validate message: {}", validateMsgFlight);
        }
        if (validateMsgPlane != null) {
            modelMap.put("validateMsgPlane", validateMsgPlane);
            log.info("Validate message: {}", validateMsgPlane);
        }
        if (validateMsgTeam != null) {
            modelMap.put("validateMsgTeam", validateMsgTeam);
            log.info("Validate message: {}", validateMsgTeam);
        }
        if (validateMsgOffice != null) {
            modelMap.put("validateMsgOffice", validateMsgOffice);
            log.info("Validate message: {}", validateMsgOffice);
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
        modelMap.addAttribute("planes", planeRepository.findAllByCompany(springUser.getUser().getCompany()));
        log.info("List of planes sent to HTML");
        return "/manager/moreDetails";
    }


    @PostMapping("/addFlight")
    public String addFlight(@Validated FlightDto flightDto, @RequestParam("plane") int planeId, @AuthenticationPrincipal SpringUser springUser,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("validateMsgFlight", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/manager/moreDetails";
        }
        flightService.save(flightDto, springUser, planeId);
        return "redirect:/manager/moreDetails";
    }


    @PostMapping("/addTeam")
    public String addFlight(@Validated TeamDto teamDto, @AuthenticationPrincipal SpringUser springUser,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("validateMsgTeam", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/manager/moreDetails";
        }
        log.info("Attempting to add team with data: {}", teamDto);
        TeamMember savedTeamMember = teamService.save(teamDto, springUser);
        if (savedTeamMember != null) {
            log.info("Team member added successfully with ID: {}", savedTeamMember.getId());
            return "redirect:/manager";
        } else {
            return "redirect:/manager/moreDetails";
        }

    }
    @GetMapping("/planes")
    public String PlanesOfTheCompany(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        modelMap.addAttribute("planes", planeService.getAllPlanesByCompany(springUser.getUser().getCompany()));
        return "/manager/ownPlanes";
    }

    @GetMapping("/update/plane/{id}")
    public String planePageForUpdate(@PathVariable("id") int id, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        modelMap.addAttribute("plane", planeService.getPlane(id,springUser.getUser().getCompany()));
        return "/manager/updatePlane";
    }

    @PostMapping("/update/plane")
    public String planeUpdate(@ModelAttribute PlaneUpdateDto planeUpdateDto, @RequestParam("picture") MultipartFile multipartFile, @AuthenticationPrincipal SpringUser springUser) {
        planeService.updatePlane(planeUpdateDto,multipartFile,springUser.getUser().getCompany());
        return "redirect:/manager/plane/"+planeUpdateDto.getId();
    }
    @GetMapping("/plane/{id}")
    public String planePage(@PathVariable("id") int id, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        modelMap.addAttribute("plane", planeService.getPlane(id,springUser.getUser().getCompany()));
        return "/manager/plane";
    }

    @GetMapping("/flights")
    public String flightPage(ModelMap modelMap,@AuthenticationPrincipal SpringUser springUser){
        modelMap.addAttribute("flights",flightService.findExistingFlights(springUser.getUser().getCompany(), Status.ARRIVED));
        return "manager/flights";
    }
    @GetMapping("/update/flight/{flightId}")
    public String ChangeFlightPage(@PathVariable("flightId") int flightId, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        modelMap.addAttribute("flight", flightService.findCompanyFlight(flightId, springUser.getUser().getCompany()));
        modelMap.addAttribute("planes", planeService.getAllPlanesByCompany(springUser.getUser().getCompany()));
        return "manager/updateFlight";
    }

    @PostMapping("/update/flight")
    public String ChangeFlight(@AuthenticationPrincipal SpringUser springUser,
                               @RequestParam("plane") int planeId,
                               @ModelAttribute("updateFlightDto") UpdateFlightDto updateFlightDto
    ) {
        flightService.changeFLight(updateFlightDto, planeId, springUser.getUser().getCompany());
        return "redirect:/manager/flights";
    }
}
