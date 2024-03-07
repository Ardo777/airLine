package com.example.airlineproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String homePage() {
        return "index";
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
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    @GetMapping("/companies")
    public String companiesPage() {
        return "companies";
    }@GetMapping("/addCompany")
    public String addCompanyPage() {
        return "addCompany";
    }
}
