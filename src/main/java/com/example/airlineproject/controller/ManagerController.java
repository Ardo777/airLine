package com.example.airlineproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ManagerController {
    @GetMapping("/manager")
    public String managerPage() {
        return "/manager/index";
    }

    @GetMapping("/manager/moreDetails")
    public String moreDetails() {
        return "/manager/moreDetails";
    }

    @GetMapping("/manager/addFlight")
    public String addFlight() {
        return "/manager/flight";
    }


}
