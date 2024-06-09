package com.example.airlineproject.controller;

import com.example.airlineproject.dto.ChangePasswordDto;
import com.example.airlineproject.dto.CompanyFewDetailsDto;
import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.Card;
import com.example.airlineproject.entity.ChatRoom;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.UserDto;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.mapper.CompanyMapper;
import com.example.airlineproject.mapper.FlightMapper;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.*;
import com.example.airlineproject.util.FileUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final FileUtil fileUtil;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final SubscribeService subscribeService;
    private final FlightService flightService;
    private final FlightMapper flightMapper;
    private final CompanyMapper companyMapper;
    private final CardService cardService;

    @GetMapping("/register")
    private String registration(@RequestParam(value = "emailMsg", required = false) String emailMsg,
                                @RequestParam(value = "passwordErrorMsg", required = false) String passwordErrorMsg,
                                @RequestParam(value = "errorCode", required = false) String errorCode,
                                @RequestParam(value = "msg", required = false) String msg,
                                @RequestParam(value = "validateMsg", required = false) String validateMsg,
                                ModelMap modelMap) {
        if (emailMsg != null) {
            modelMap.put("emailMsg", emailMsg);
        }
        if (msg != null) {
            modelMap.put("msg", msg);
        }
        if (passwordErrorMsg != null) {
            modelMap.put("passwordErrorMsg", passwordErrorMsg);
        }
        if (errorCode != null) {
            modelMap.put("errorCode", errorCode);
        }
        if (validateMsg !=null){
            modelMap.put("validateMsg", validateMsg);
        }
        log.info("Rendering registration page with emailMsg: {}, passwordErrorMsg: {}, errorCode: {}", emailMsg, passwordErrorMsg, errorCode);
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
    public String registration(
            @Validated UserRegisterDto userRegisterDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("validateMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/user/register";
        }
        UserResponseDto byEmail = userService.findByEmail(userRegisterDto.getEmail());
        if (byEmail != null) {
            String emailMsg = "User with this email " + userRegisterDto.getEmail() + " already  exist";
            log.warn(emailMsg);
            redirectAttributes.addFlashAttribute("emailMsg", emailMsg);
            return "redirect:/user/register";
        }
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            String msg = "Password mismatch";
            log.warn(msg);
            redirectAttributes.addFlashAttribute("msg", msg);
            return "redirect:/user/register";
        } else {
            User saveUser = userService.save(userRegisterDto, userRegisterDto.getPicture());
            log.info("User registered successfully: {}", saveUser.getEmail());
            return "redirect:/user/register/verification/" + saveUser.getEmail();
        }
    }

    @GetMapping("/register/verification/{mail}")
    public String verificationPage(@PathVariable("mail") String mail, ModelMap modelMap) {
        UserResponseDto userResponseDto = userService.findByEmail(mail);
        modelMap.addAttribute("user", userResponseDto);
        return "mail/mailVerification";
    }

    @PostMapping("/register/verification")
    public String verification(@RequestParam("id") int id,
                               @RequestParam("verificationCode") String verificationCode) {

        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            if (user.getVerificationCode().equals(verificationCode)) {
                user.setActive(true);
                userRepository.save(user);
                User randomAdmin = userService.findRandomAdmin();
                String chatId = chatRoomService.createChatId(user.getEmail(), randomAdmin.getEmail());
                Card card= Card.builder()
                        .balance(10000)
                        .idNumber(fileUtil.createVerificationCode())
                        .user(user)
                        .build();
                cardService.save(card);
                log.info("ChatRoom already crated with {}", chatId);
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

        if (springUser.getUser() != null) {
            if (springUser.getUser().getRole() == UserRole.ADMIN) {
                return "/admin/users";
            } else if (springUser.getUser().getRole() == UserRole.MANAGER) {
                return "/manager/flights";
            }
        }
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session, @AuthenticationPrincipal SpringUser user) {
        persistentTokenRepository.removeUserTokens(user.getUsername());
        session.invalidate();
        return "redirect:/user/login";
    }


    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestParam("email") String email) {
        UserResponseDto userResponseDto = userService.findByEmail(email);
        if (userResponseDto == null) {
            String emailMsg = "User with this email does not exist";
            return "redirect:/user/forgetPassword?emailMsg=" + emailMsg;
        }
        userService.findByEmail(email).setVerificationCode(fileUtil.createVerificationCode());
        mailService.sendRecoveryMail(userResponseDto.getEmail());
        return "redirect:/user/codeVerification/" + userResponseDto.getId();
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

    @GetMapping("/profile")
    public String userProfilePage(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if (springUser != null) {
            User user = springUser.getUser();
            if (user != null) {
                modelMap.addAttribute("user", user);
                log.info("User profile page accessed successfully for user: {}", user.getEmail());
                return "userProfile";
            } else {
                log.warn("No user details found for authenticated user.");
                return "redirect:/user/login";
            }
        } else {
            log.warn("No authenticated user found while accessing the user profile page.");
            return "redirect:/user/login";
        }
    }


    @PostMapping("/update")
    public String userProfile(@ModelAttribute User user, @AuthenticationPrincipal SpringUser springUser, @RequestParam(value = "picture", required = false) MultipartFile multipartFile) throws IOException {
        userService.update(user, springUser, multipartFile);
        log.info("User profile updated successfully for user: {}", user.getEmail());
        return "redirect:/";
    }


    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute ChangePasswordDto changePasswordDto, @AuthenticationPrincipal SpringUser springUser) {
        boolean result = userService.changePassword(changePasswordDto, springUser);
        log.info("Password changed successfully for user: {}", springUser.getUser().getEmail());
        return "redirect:/user/profile";
    }

    @GetMapping("/emailUpdate/{email}")
    public String emailUpdatePage(@AuthenticationPrincipal SpringUser springUser, @PathVariable("email") String email, ModelMap modelMap) {
        if (springUser.getUser() != null) {
            log.debug("Accessed emailUpdatePage method with email: {}", email);
            userService.updateEmail(springUser, email);
            modelMap.addAttribute("email", email);
            return "userUpdateMail";
        } else return "redirect:/user/login";
    }

    @PostMapping("/emailUpdate")
    public String emailUpdate(@AuthenticationPrincipal SpringUser springUser, @RequestParam("email") String email, @RequestParam("verificationCode") String verificationCode) {
        if (springUser.getUser() != null) {
            userService.processEmailUpdate(springUser, email, verificationCode);
            return "redirect:/user/profile";
        } else return "redirect:/user/login";
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findConnectedUsers(@AuthenticationPrincipal SpringUser springUser) {
        List<ChatRoom> byRecipientId = chatRoomService.findByRecipientId(springUser.getUser().getEmail());
        List<UserDto> users = new ArrayList<>();
        for (ChatRoom chatRoom : byRecipientId) {
            String senderId = chatRoom.getSenderId();
            users.add(UserDto.builder()
                    .nickName(senderId)
                    .build());
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/delete")
    public String deleteUserPage() {
        return "validPassword";
    }

    @PostMapping("/delete")
    public String deleteUser(@AuthenticationPrincipal SpringUser springUser, @RequestParam("password") String password) {
        boolean delete = userService.delete(springUser, password);
        if (delete) {
            return "redirect:/user/logout";
        }
        return "redirect:/user/profile";
    }


    @GetMapping("/account/restore")
    public String restoreAccountPage() {
        log.info("Navigated to /account/restore page");
        return "accountRestore";
    }

    @PostMapping("/account/restore")
    public String verificationSuccessfully(@RequestParam("verificationCode") String verificationCode, @RequestParam("email") String email) {
        log.info("POST request to /account/restore with email: {} and verificationCode: {}", email, verificationCode);
        if (verificationCode != null && !verificationCode.isEmpty()) {
            boolean restored = userService.restoreUser(email, verificationCode);
            if (restored) {
                return "redirect:/user/login";
            }
            log.warn("Verification failed for email: {}", email);
        }
        return "redirect:/user/login";
    }


    @PostMapping("/verificationRestore")
    public String verificationRestore(@RequestParam("email") String email, ModelMap modelMap) {
        log.info("POST request to /verificationRestore with email: {}", email);
        if (email != null && !email.isEmpty()) {
            try {
                userService.verify(email);
                log.info("Verification initiated for email: {}", email);
                modelMap.addAttribute("email", email);
                String redirectUrl = "redirect:/user/userVerificationPage?email=" + email;
                log.info("Redirecting to: {}", redirectUrl);
                return redirectUrl;
            } catch (Exception e) {
                log.error("Error during verification process for email: {}", email, e);
                return "redirect:/user/login";
            }
        }
        log.warn("Email parameter is null or empty");
        return "redirect:/user/login";
    }

    @GetMapping("/userVerificationPage")
    public String userVerificationPage(@RequestParam("email") String email, ModelMap modelMap) {
        log.info("Navigated to /userVerificationPage with email: {}", email);
        modelMap.addAttribute("email", email);
        return "mail/userVerification";
    }


    @GetMapping("/subscribe/{companyId}")
    public String subscribe(@PathVariable("companyId") int companyId, @AuthenticationPrincipal SpringUser user, ModelMap modelMap) {
        userService.subscribeToCompany(companyId, user.getUser());
        return "redirect:/user/news";
    }

    @GetMapping("/news")
    public String subscribe(@AuthenticationPrincipal SpringUser user, ModelMap modelMap) {
        List<CompanyFewDetailsDto> companiesByUserFromSubscribe = subscribeService.findCompaniesByUserFromSubscribe(user.getUser());
        modelMap.addAttribute("subscriptions", companiesByUserFromSubscribe);
        modelMap.addAttribute("flights", flightService.flightsOfSubscriptions(companyMapper.CompanyFewDetailsDtoListToCompany(companiesByUserFromSubscribe)));
        return "news";
    }
}
