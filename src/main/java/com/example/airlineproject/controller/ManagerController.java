package com.example.airlineproject.controller;


import com.example.airlineproject.dto.ChangeFlightDto;
import com.example.airlineproject.dto.FlightDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.entity.enums.Profession;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.service.OfficeService;
import com.example.airlineproject.service.PlaneService;
import com.example.airlineproject.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

    private final OfficeService officeService;
    private final PlaneService planeService;
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
                              @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
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
        modelMap.addAttribute("planes", planeRepository.findAllByCompany(springUser.getUser().getCompany()));
        log.info("List of planes sent to HTML");
        return "/manager/moreDetails";
    }

    @PostMapping("/addFlight")
    public String addFlight(@ModelAttribute FlightDto flightDto, @RequestParam("plane") int planeId, @AuthenticationPrincipal SpringUser springUser) {
        flightService.save(flightDto, springUser, planeId);
        return "redirect:/manager/moreDetails";
    }


    @PostMapping("/addAirPlane")
    public String addAirPlane(@RequestParam("model") String model,
                              @RequestParam("maxBaggage") double maxBaggage,
                              @RequestParam("countBusiness") int countBusiness,
                              @RequestParam("countEconomy") int countEconomy,
                              @RequestParam("countRow") int countRow,
                              @AuthenticationPrincipal SpringUser springUser,
                              @RequestParam("picture") MultipartFile multipartFile) throws IOException {

        log.debug("Model: {}", model);
        log.debug("Max Baggage: {}", maxBaggage);
        log.debug("Count Business Places: {}", countBusiness);
        log.debug("Count Economy Places: {}", countEconomy);
        log.debug("Count Row: {}", countRow);
        log.debug("User: {}", springUser.getUsername());
        Plane plane = planeService.createPlane(model, maxBaggage, countBusiness, countEconomy, countRow, multipartFile);
        Boolean isPlaneExist = planeService.isPlaneExist(plane, springUser.getUser());
        plane.setCompany(springUser.getUser().getCompany());
        if (isPlaneExist) {
            String planeErrorMsg = "A plane with these parameters was previously added to your company";
            log.warn(planeErrorMsg);
            return "redirect:/manager/moreDetails?planeErrorMsg=" + planeErrorMsg;
        }
        planeService.saveAirPlane(plane, multipartFile);
        String planeSuccessMsg = "Airplane added successfully";
        log.info(planeSuccessMsg);
        return "redirect:/manager/moreDetails?planeSuccessMsg=" + planeSuccessMsg;
    }



    @PostMapping("/addOffice")
    public String addOffice(@ModelAttribute Office office, @AuthenticationPrincipal SpringUser springUser) {
        Boolean isOfficeExist = officeService.isOfficeExist(office);
        office.setCompany(springUser.getUser().getCompany());
        if (isOfficeExist) {
            String officeErrorMsg = "An office already exists at this address";
            log.warn("Office already exists at this address");
            return "redirect:/manager/moreDetails?officeErrorMsg=" + officeErrorMsg;
        }
        officeService.saveOffice(office, springUser.getUser());
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
        //I send changeFlightDto and company together because manager can enter into the <inspect> and change that flight id and receive another flight of other company
        flightService.changeFLight(changeFlightDto, planeId, springUser.getUser().getCompany());
        return "redirect:/manager/flights";
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


    @GetMapping("/teamMembers")
    public String myCompanyPage(@AuthenticationPrincipal SpringUser springUser,
                                ModelMap modelMap) {
        modelMap.addAttribute("teamMembers", teamService.findTeamMemberByCompanyAndActive(springUser.getUser().getCompany()));
        return "/manager/teamMembers";
    }

    @GetMapping("/teamMembers/delete/{id}")
    public String deleteTeamMember(@PathVariable("id") int id) {
        teamService.deleteTeamMember(id);
        log.info("Team Member deleted successfully,active is false");
        return "redirect:/manager/teamMembers";
    }

    @GetMapping("/teamMembers/change/{id}")
    public String changeTeamMemberPage(@PathVariable("id") int id,
                                       ModelMap modelMap) {
        TeamMember teamMember = teamService.findById(id);
        if (teamMember != null) {
            if (teamMember.isActive()) {
                modelMap.addAttribute("teamMember", teamMember);
                return "/manager/teamMemberChange";
            }
        }
        return "/manager/teamMembers";
    }

    @PostMapping("/teamMembers/change")
    public String changeTeamMember(@RequestParam("id") int id,
                                   @RequestParam("name") String name,
                                   @RequestParam("surname") String surname,
                                   @RequestParam("profession") Profession profession
    ) {
        log.info("Changing team member with ID {}, name {}, surname {}, profession {}", id, name, surname, profession);

        teamService.changeTeamMember(id, name, surname, profession);
        return "redirect:/manager/teamMembers";
    }

    @GetMapping("/office/change")
    public String changeOfficePage(@AuthenticationPrincipal SpringUser springUser,
                                   ModelMap modelMap) {
        Office office = officeService.findByCompany(springUser.getUser().getCompany());
        modelMap.addAttribute("office", office);
        return "/manager/officeChange";
    }

    @PostMapping("/office/change")
    public String changeOffice(@RequestParam("id") int id,
                               @RequestParam("country") String country,
                               @RequestParam("city") String city,
                               @RequestParam("street") String street,
                               @RequestParam("workStartTime") Date workStartTime,
                               @RequestParam("workEndTime") Date workEndTime,
                               @RequestParam("phone") String phone
    ) {
        log.info("Changing office with ID {}, country {}, city {}, street {}, workStartTime {}, workEndTime {}, phone {}",
                id, country, city, street, workStartTime, workEndTime, phone);
        officeService.changeOffice(id, country, city, street, workStartTime, workEndTime, phone);
        return "redirect:/manager/index";
    }

    @GetMapping("/planes")
    public String PlanesOfTheCompany(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        modelMap.addAttribute("planes", planeService.allPlanesOfTheCompany(springUser.getUser().getCompany()));
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
}
