package com.example.airlineproject.controller;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/company")
    public String companiesPage(@AuthenticationPrincipal SpringUser springUser,
                                @RequestParam(value = "emailMsg", required = false) String emailMsg,
                                ModelMap modelMap) {
        if (emailMsg != null) {
            modelMap.put("emailMsg", emailMsg);
        }
        User user = springUser.getUser();
        if (user == null) {
            return "/index";
        }
        modelMap.addAttribute(user);
        return "addCompany";
    }

    @PostMapping("/company/register")
    public String companyRegister(@AuthenticationPrincipal SpringUser springUser, @RequestParam("name") String name, @RequestParam("email") String email,
                                  @RequestParam(required = false, value = "picName") MultipartFile multipartFile) throws IOException {
        Company company = companyService.byEmail(email);
        if (company != null) {
            String emailMsg = "Company with this email " + email + " already  exist";
            return "redirect:/company?emailMsg=" + emailMsg;
        }
        User user = springUser.getUser();
        Company save = companyService.save(Company.builder()
                .name(name)
                .email(email)
                .user(user)
                .build(), multipartFile);
        if (save == null) {
            String emailMsg = "this user " + user.getName() + " " + user.getSurname() + " already has his own company or this company name is already taken";
            return "redirect:/company?emailMsg=" + emailMsg;
        }
        return "redirect:/";
    }
}
