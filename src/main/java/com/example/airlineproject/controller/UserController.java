package com.example.airlineproject.controller;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/register")
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
        log.info("Rendering registration page with emailMsg: " + emailMsg
                + ", passwordErrorMsg: " + passwordErrorMsg
                + ", errorCode: " + errorCode);
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "successMsg", required = false) String successMsg,
                            @RequestParam(value = "errorMessage", required = false) String errorMessage,
                            ModelMap modelMap) {
        if (successMsg != null) {
            modelMap.put("successMsg", successMsg);
        } else if (errorMessage != null) {
            modelMap.put("errorMessage", errorMessage);
        }
        return "login";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute User user,
                               @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        if (byEmail.isPresent() && byEmail.get().isActive()) {
            String emailMsg = "User with this email " + user.getEmail() + " already  exist";
            log.warn(emailMsg);
            return "redirect:/user/register?emailMsg=" + emailMsg;
        }
        if (user.getPassword().length() < 6) {
            String passwordErrorMsg = "Password cannot be shorter than 6 characters";
            log.warn(passwordErrorMsg);
            return "redirect:/user/register?passwordErrorMsg=" + passwordErrorMsg;
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            String msg = "Password mismatch";
            log.warn(msg);
            return "redirect:/user/register?msg=" + msg;
        } else {
            User saveUser = userService.save(user, multipartFile);
            log.info("User registered successfully: " + saveUser.getEmail());
            return "redirect:/user/register/verification/" + saveUser.getEmail();
        }

    }

    @GetMapping("/register/verification/{mail}")
    public String verificationPage(@PathVariable("mail") String mail, ModelMap modelMap) {

        Optional<User> userOptional = userService.findByEmail(mail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            modelMap.addAttribute("user", user);
        } else {
            log.warn("User with email " + mail + " not found.");
        }
        return "mail/mailVerification";
    }

    @PostMapping("/register/verification")
    public String verification(@RequestParam("id") int id, @RequestParam("verificationCode") String verificationCode) {

        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            if (user.getVerificationCode().equals(verificationCode)) {
                user.setActive(true);
                userRepository.save(user);
                String successMsg = "Verification was successful!";
                log.info(successMsg);
                return "redirect:/user/login?successMsg=" + successMsg;
            }
        } else {
            userService.deleteById(id);
            String errorCode = "Invalid verification code. Please register again.";
            log.warn(errorCode);
            return "redirect:/user/register?errorCode=" + errorCode;
        }
        return null;
    }


    @GetMapping("/login/successfully")
    public String successLoginPage(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser.getUser().getRole() == UserRole.ADMIN) {
            return "redirect:/admin";
        } else if (springUser.getUser().getRole() == UserRole.MANAGER) {
            return "redirect:/manager";
        }
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }


    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestParam("email") String email) {
        Optional<User> byEmail = userService.findByEmail(email);
        if (byEmail.isEmpty()) {
            String emailMsg = "User with this email does not exist";
            return "redirect:/user/forgetPassword?emailMsg=" + emailMsg;
        }
        User user = byEmail.get();
        userService.verificationCodeSending(user, email);
        return "redirect:/user/codeVerification/" + user.getId();
    }

    @GetMapping("/forgetPassword")
    public String forgetPasswordPage(@RequestParam(value = "errorMessage", required = false) String errorMessage,
                                     @RequestParam(value = "emailMsg", required = false) String emailMsg,
                                     ModelMap modelMap) {
        if (emailMsg != null) {
            modelMap.put("emailMsg", emailMsg);
        }
        if (errorMessage != null) {
            modelMap.put("errorMessage", errorMessage);
        }
        return "forgetPassword";
    }

    @PostMapping("/codeVerification")
    public String codeVerification(@RequestParam("id") int id,
                                   @RequestParam("code") String code) {
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            if (user.getVerificationCode().equals(code)) {
                return "redirect:/user/recovery/" + id;
            }
        }
        String errorMessage = "The verification code was entered incorrectly";
        return "redirect:/user/forgetPassword?errorMessage=" + errorMessage;
    }

    @GetMapping("/codeVerification/{id}")
    public String codeVerificationPage(@PathVariable("id") int id,
                                       ModelMap modelMap) {
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            modelMap.addAttribute("user", user);
        }
        return "codeVerification";
    }

    @PostMapping("/recovery")
    public String recoveryPassword(@RequestParam("id") int id,
                                   @RequestParam("newPassword") String newPassword,
                                   @RequestParam("newPasswordConfirmation") String newPasswordConfirmation,
                                   ModelMap modelMap) {
        if (!newPassword.equals(newPasswordConfirmation)) {
            String mismatchMsg = "Password dont mismatch";
            return "redirect:/recovery/" + id + "?mismatchMsg=" + mismatchMsg;

        }
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            boolean recoveryPassword = userService.recoveryPassword(user, newPassword);
            if (recoveryPassword) {
                String successMsg = "Password already changed";
                modelMap.put("successMsg", successMsg);
            }
        }
        return "login";
    }

    @GetMapping("/recovery/{id}")
    public String recoveryPasswordPage(@PathVariable("id") int id,
                                       @RequestParam(value = "mismatchMsg", required = false) String mismatchMsg,
                                       ModelMap modelMap) {
        Optional<User> byId = userService.findById(id);
        if (mismatchMsg != null) {
            modelMap.put("mismatchMsg", mismatchMsg);
        }

        if (byId.isPresent()) {
            User user = byId.get();
            modelMap.addAttribute("user", user);
        }
        return "recovery";
    }
}
