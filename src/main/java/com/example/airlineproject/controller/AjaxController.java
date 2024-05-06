package com.example.airlineproject.controller;

import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.City;
import com.example.airlineproject.service.CityService;
import com.example.airlineproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AjaxController {

    private final CityService cityService;
    private final UserService userService;

    @GetMapping("/cities")
    public List<City> getCities(@RequestParam("countryId") int countryId) {
        return cityService.getCitiesByCountry(countryId);
    }

    @GetMapping("/filter")
    public List<UserResponseDto> getByFilter(@RequestParam("keyword") String keyword) {
        log.info("User searching success");
        return userService.getAllByFilter(keyword);
    }


}
