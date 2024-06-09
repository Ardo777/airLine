package com.example.airlineproject.controller;

import com.example.airlineproject.dto.PlaneAddDto;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.PlaneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class PlaneController {

    private final PlaneService planeService;

    @PostMapping("/addAirPlane")
    public String addAirPlane(@ModelAttribute PlaneAddDto planeAddDto,
                              @AuthenticationPrincipal SpringUser springUser,
                              RedirectAttributes redirectAttributes,
                              BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("validateMsgPlane", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/manager/moreDetails";
        }

        log.debug("Adding plane : {}", planeAddDto);
        log.debug("User: {}", springUser.getUsername());
        Boolean isPlaneExist = planeService.isPlaneExist(planeAddDto, springUser.getUser());
        if (isPlaneExist) {
            String planeErrorMsg = "A plane with these parameters was previously added to your company";
            log.warn(planeErrorMsg);
            redirectAttributes.addFlashAttribute("planeErrorMsg", planeErrorMsg);
            return "redirect:/manager/moreDetails";
        }
        planeService.saveAirPlane(planeAddDto, planeAddDto.getPicture(), springUser);
        String planeSuccessMsg = "Airplane added successfully";
        log.info(planeSuccessMsg);
        redirectAttributes.addFlashAttribute("planeSuccessMsg", planeSuccessMsg);
        return "redirect:/manager/moreDetails";
    }

}
