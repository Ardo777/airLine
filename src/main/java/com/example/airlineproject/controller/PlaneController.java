package com.example.airlineproject.controller;

import com.example.airlineproject.dto.PlaneAddDto;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.PlaneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
                              @RequestParam("picture") MultipartFile multipartFile) throws IOException {

        log.debug("Adding plane : {}", planeAddDto);
        log.debug("User: {}", springUser.getUsername());
        PlaneAddDto plane = planeService.createPlane(planeAddDto, multipartFile);
        Boolean isPlaneExist = planeService.isPlaneExist(planeAddDto, springUser.getUser());
        if (isPlaneExist) {
            String planeErrorMsg = "A plane with these parameters was previously added to your company";
            log.warn(planeErrorMsg);
            return "redirect:/manager/moreDetails?planeErrorMsg=" + planeErrorMsg;
        }
        planeService.saveAirPlane(plane, multipartFile, springUser);
        String planeSuccessMsg = "Airplane added successfully";
        log.info(planeSuccessMsg);
        return "redirect:/manager/moreDetails?planeSuccessMsg=" + planeSuccessMsg;
    }

}
