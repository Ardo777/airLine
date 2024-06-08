package com.example.airlineproject.controller;

import com.example.airlineproject.entity.*;
import com.example.airlineproject.exception.RatingOutOfBoundException;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CommentService;
import com.example.airlineproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;
    private final CommentService commentService;

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
        modelMap.addAttribute(user);
        return "addCompany";
    }

    @PostMapping("/company/register")
    public String companyRegister(@AuthenticationPrincipal SpringUser springUser,
                                  @RequestParam("name") String name,
                                  @RequestParam("email") String email,
                                  @RequestParam("picName") MultipartFile picFile,
                                  @RequestParam("picture") MultipartFile certificateFile) throws IOException {
        log.info("Received request to register company");
        String msg = companyService.registerCompany(springUser.getUser(), name, email, picFile, certificateFile);
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
        companyService.accept(id);
        return "redirect:/company/requests";
    }

    @GetMapping("/company/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        companyService.delete(id);
        return "redirect:/companies";
    }

    @GetMapping("/allCompanies")
    public String allCompaniesPage(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "successMsg", required = false) String successMsg,
            @RequestParam(value = "errorMsg", required = false) String errorMsg,
            ModelMap modelMap) {
        if (successMsg != null) {
            modelMap.put("successMsg", successMsg);
        }
        if (errorMsg != null) {
            modelMap.put("errorMsg", errorMsg);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Company> companies = companyService.findAllByRating(pageable);
        modelMap.addAttribute("companies", companies);
        int totalPages = companies.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        return "allCompanies";
    }

    @PostMapping("/rating")
    public String starRatingCompany(@RequestParam("companyId") int companyId,
                                    @RequestParam("rating") int rating,
                                    @AuthenticationPrincipal SpringUser springUser) {
        if (rating > 5 || rating <= 0) {
            throw new RatingOutOfBoundException();
        }
        Boolean save = companyService.starRatingSave(companyId, rating, springUser.getUser());
        if (save) {
            String successMsg = "Star rating add successfully";
            return "redirect:/allCompanies?successMsg=" + successMsg;
        }
        String errorMsg = "You have already rated this company";
        return "redirect:/allCompanies?errorMsg=" + errorMsg;
    }


    @GetMapping("/company/{id}")
    public String companyPage(@PathVariable("id") int id,
                              ModelMap modelMap) {
        Company company = companyService.findById(id);
        modelMap.addAttribute("company", company);
        modelMap.addAttribute("comments", commentService.getAllCommentsByCompanyId(id));
        return "company";
    }


    @GetMapping("/company/{id}/planes")
    public String planesPage(@PathVariable("id") int id, ModelMap modelMap) {
        Company company = companyService.findById(id);
        List<Plane> planes = company.getPlanes();
        if (!planes.isEmpty()) {
            modelMap.addAttribute("company", company);
            modelMap.addAttribute("planes", planes);
        } else {
            modelMap.addAttribute("company", company);
        }
        return "planes";
    }

    @GetMapping("/company/{id}/members")
    public String membersPage(@PathVariable("id") int id, ModelMap modelMap) {
        Company company = companyService.findById(id);
        List<TeamMember> teamMembers = company.getTeamMembers();
        if (teamMembers != null && !teamMembers.isEmpty()) {
            modelMap.addAttribute("company", company);
            modelMap.addAttribute("teamMembers", teamMembers);
        } else {
            modelMap.addAttribute("company", company);
        }
        return "teamMembers";
    }

    @GetMapping("/company/{id}/offices")
    public String officesPage(@PathVariable("id") int id, ModelMap modelMap) {
        Company company = companyService.findById(id);
        List<Office> offices = company.getOffice();
        if (!offices.isEmpty()) {
            modelMap.addAttribute("company", company);
            modelMap.addAttribute("offices", offices);
        } else {
            modelMap.addAttribute("company", company);
        }
        return "offices";
    }

    @GetMapping("/company/{id}/flights")
    public String flightsPage(@PathVariable("id") int id, ModelMap modelMap) {
        Company company = companyService.findById(id);
        List<Flight> flights = company.getFlights();
        if (!flights.isEmpty()) {
            modelMap.addAttribute("company", company);
            modelMap.addAttribute("flights", flights);
        } else {
            modelMap.addAttribute("company", company);
        }
        return "flights";
    }

    @PostMapping("/allCompanies/search")
    public String searchCompanies(@RequestParam("keyword") String keyword,
                                  ModelMap modelMap) {
        if (keyword.isBlank()) {
            return "redirect:/allCompanies";
        }
        List<Company> companies = companyService.getAllCompaniesByFilter(keyword);
        modelMap.addAttribute("companies", companies);

        return "allCompanies";
    }

}

