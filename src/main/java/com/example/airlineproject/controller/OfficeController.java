package com.example.airlineproject.controller;

import com.example.airlineproject.dto.OfficeChangeDto;
import com.example.airlineproject.entity.Country;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CountryService;
import com.example.airlineproject.service.OfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class OfficeController {

    private final OfficeService officeService;
    private final CountryService countryService;

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


    @GetMapping("/office/change")
    public String changeOfficePage(@AuthenticationPrincipal SpringUser springUser,
                                   ModelMap modelMap) {
        Office office = officeService.findByCompany(springUser.getUser().getCompany());
        modelMap.addAttribute("office", office);
        modelMap.addAttribute("countries", countryService.getAllCountries());
        return "/manager/officeChange";
    }

    @PostMapping("/office/change")
    public String changeOffice(@ModelAttribute OfficeChangeDto officeChangeDto
                               ) {
        log.info("Changing office with ID {}, country {}, city {}, street {}, workStartTime {}, workEndTime {}, phone {}",
                officeChangeDto.getId(),officeChangeDto.getCountry().getName(),
                officeChangeDto.getCity().getName(),officeChangeDto.getStreet(),
                officeChangeDto.getWorkStartTime(),officeChangeDto.getWorkEndTime(),
                officeChangeDto.getPhone());
        officeService.changeOffice(officeChangeDto);
        return "redirect:/manager/index";
    }
}
