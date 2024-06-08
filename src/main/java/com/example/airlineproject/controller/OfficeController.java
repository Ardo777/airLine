package com.example.airlineproject.controller;

import com.example.airlineproject.dto.OfficeChangeDto;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CountryService;
import com.example.airlineproject.service.OfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class OfficeController {

    private final OfficeService officeService;
    private final CountryService countryService;

    @PostMapping("/addOffice")
    public String addOffice(@Validated Office office, @AuthenticationPrincipal SpringUser springUser,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("validateMsgOffice", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/manager/moreDetails";
        }
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


    @GetMapping("/office/update/{id}")
    public String changeOfficePage(@PathVariable("id") int id,
                                   ModelMap modelMap) {
        Office office = officeService.findById(id);
        modelMap.addAttribute("office", office);
        modelMap.addAttribute("countries", countryService.getAllCountries());
        return "/manager/officeUpdate";
    }

    @PostMapping("/office/update")
    public String changeOffice(@ModelAttribute OfficeChangeDto officeChangeDto
    ) {
        log.info(officeChangeDto.toString());
        String officeSuccessMsg = officeService.changeOffice(officeChangeDto);
        return "redirect:/manager/offices?officeSuccessMsg=" + officeSuccessMsg;
    }

    @GetMapping("/offices")
    public String allOffices(@RequestParam(value = "officeSuccessMsg", required = false) String officeSuccessMsg,
                             @AuthenticationPrincipal SpringUser springUser,
                             ModelMap modelMap) {
        if (officeSuccessMsg != null) {
            modelMap.put("officeSuccessMsg", officeSuccessMsg);
        }
        modelMap.addAttribute("offices", officeService.getAllOfficesByUser(springUser.getUser()));
        return "/manager/offices";
    }

}
