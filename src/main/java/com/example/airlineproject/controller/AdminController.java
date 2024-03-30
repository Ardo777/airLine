package com.example.airlineproject.controller;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;

    @GetMapping("/admin")
    public String adminPage() {
        return "/admin/index";
    }

    @GetMapping("/admin/users")
    public String usersPage(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "15", required = false) int size,
            ModelMap modelMap) {
        log.info("Fetching users for page " + page + " with size " + size);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userService.findAll(pageable);
        modelMap.addAttribute("users", usersPage);
        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        log.info("Users fetched successfully");
        return "/admin/users";
    }


    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        log.info("Deleting user with id: " + id);
        userService.deleteById(id);
        log.info("User deleted successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user/{id}")
    public String profileUser(@PathVariable("id") int id, ModelMap modelMap) {
        log.info("Fetching user profile for id: " + id);

        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            modelMap.addAttribute("user", user);
            log.info("User profile fetched successfully");
        } else {
            log.warn("User with id " + id + " not found.");
        }

        return "/admin/user";
    }

}
