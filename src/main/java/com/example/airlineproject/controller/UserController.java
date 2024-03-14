package com.example.airlineproject.controller;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/user/register")
    private String registration(@RequestParam(value = "emailMsg", required = false) String emailMsg,
                                @RequestParam(value = "passwordErrorMsg", required = false) String passwordErrorMsg,
                                @RequestParam(value = "errorCode", required = false) String errorCode,
                                ModelMap modelMap) {
        if (emailMsg != null) {
            modelMap.put("emailMsg", emailMsg);
        }
        if (passwordErrorMsg != null) {
            modelMap.put("passwordErrorMsg", passwordErrorMsg);
        }
        if (errorCode != null) {
            modelMap.put("errorCode", errorCode);
        }
        return "register";
    }

    @GetMapping("/user/login")
    public String loginPage(@RequestParam(value = "successMsg", required = false) String successMsg, ModelMap modelMap) {
        if (successMsg != null) {
            modelMap.put("successMsg", successMsg);
        }
        return "login";
    }

    @PostMapping("/user/register")
    public String registration(@ModelAttribute User user,
                               @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        if (byEmail.isPresent() && byEmail.get().isActive()) {
            String emailMsg = "User with this email " + user.getEmail() + " already  exist";
            return "redirect:/user/register?emailMsg=" + emailMsg;
        }
        if (user.getPassword().length() < 6) {
            String passwordErrorMsg = "Password cannot be shorter than 6 characters";
            return "redirect:/user/register?passwordErrorMsg=" + passwordErrorMsg;
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            String msg = "Password mismatch";
            return "redirect:/user/register?msg=" + msg;
        } else {
            User saveUser = userService.save(user, multipartFile);
            return "redirect:/user/register/verification/" + saveUser.getEmail();
        }

    }

    @GetMapping("/user/register/verification/{mail}")
    public String verificationPage(@PathVariable("mail") String mail, ModelMap modelMap) {
        Optional<User> userOptional = userService.findByEmail(mail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            modelMap.addAttribute("user", user);
        }
        return "mail/mailVerification";
    }

    @PostMapping("/user/register/verification")
    public String verification(@RequestParam("id") int id, @RequestParam("verificationCode") String verificationCode) {

        Optional<User> byId = userService.findById(id);
        User user = byId.get();
        if (user.getVerificationCode().equals(verificationCode)) {
            user.setActive(true);
            userRepository.save(user);
            String successMsg = "Verification was successful!";

            return "redirect:/user/login?successMsg=" + successMsg;
        } else {
            userService.deleteById(id);
            String errorCode = "Invalid verification code. Please register again.";
            return "redirect:/user/register?errorCode=" + errorCode;
        }

    }
}
