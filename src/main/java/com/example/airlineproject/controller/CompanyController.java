package com.example.airlineproject.controller;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/company")
    public String companiesPage(@AuthenticationPrincipal SpringUser springUser,
                                @RequestParam(value = "emailMsg", required = false) String emailMsg,
                                ModelMap modelMap) {
        log.info("Request received for /company page");
        if (emailMsg != null) {
            log.info("Email message parameter found: {}", emailMsg);
            modelMap.put("emailMsg", emailMsg);
        }
        User user = springUser.getUser();
        if (user == null) {
            log.warn("User is not authenticated, redirecting to index page");
            return "index";
        }
        log.info("User authenticated, adding user to model");
        modelMap.addAttribute(user);
        log.info("Returning addCompany page");
        return "addCompany";
    }

    @PostMapping("/company/register")
    public String companyRegister(@AuthenticationPrincipal SpringUser springUser,
                                  @RequestParam("name") String name,
                                  @RequestParam("email") String email,
                                  @RequestParam(required = false, value = "picName") MultipartFile multipartFile) throws IOException {
        log.info("Received request to register company");
        String msg = companyService.registerCompany(springUser.getUser(), name, email, multipartFile);
        if (msg != null) {
            log.info("Company registration failed: {}", msg);
            return "redirect:/company?emailMsg=" + msg;
        }
        log.info("Company registered successfully");
        return "redirect:/";
    }

    @GetMapping("/companies")
    public String companies(ModelMap modelMap) {
        log.info("Request received for listing companies");
        List<Company> byActive = companyService.findByActive(true);
        if (byActive != null) {
            log.info("Found {} active companies", byActive.size());
            modelMap.put("companies", byActive);
        } else {
            log.info("No active companies found");
        }
        return "admin/companies";
    }

    @GetMapping("/company/requests")
    public String companyRequest(ModelMap modelMap) {
        log.info("Request received for listing company requests");
        List<Company> byActive = companyService.findByActive(false);
        if (byActive != null) {
            log.info("Found {} inactive companies", byActive.size());
            modelMap.put("companies", byActive);
        } else {
            log.info("No inactive companies found");
        }
        return "admin/companyRequests";
    }

    @GetMapping("/company/accept/{id}")
    public String accept(@PathVariable("id") int id) {
        log.info("Accept request received for company with id: {}", id);
        companyService.accept(id);
        log.info("Redirecting to /company/requests");
        return "redirect:/company/requests";
    }

    @GetMapping("/company/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        log.info("Delete request received for company with id: {}", id);
        companyService.delete(id);
        log.info("Redirecting to /company/requests");
        return "redirect:/company/requests";
    }
}
