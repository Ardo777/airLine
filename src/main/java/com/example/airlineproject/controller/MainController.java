package com.example.airlineproject.controller;

import com.example.airlineproject.dto.FlightsListResponseDto;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.FlightService;
import com.example.airlineproject.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final FileUtil fileUtil;
    private final FlightService flightService;

    @GetMapping("/")
    public String homePage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        List<FlightsListResponseDto> flightsList = flightService.findFirst10Flights();
        modelMap.addAttribute("flightsList", flightsList);
        return "index";
    }

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUtil.getPicture(picName);
    }


    @GetMapping("/hotel")
    public String hotelPage() {
        return "hotel";
    }

    @GetMapping("/news")
    public String newsPage() {
        return "news";
    }

    @GetMapping("/aboutUs")
    public String aboutUsPage() {
        return "aboutUs";
    }

    @GetMapping("/addCompany")
    public String addCompanyPage() {
        return "addCompany";
    }

}
