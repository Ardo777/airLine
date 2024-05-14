package com.example.airlineproject.controller;

import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.dto.ChangePasswordDto;
import com.example.airlineproject.entity.ChatRoom;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.entity.UserDto;
import com.example.airlineproject.repository.ChatRoomRepository;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.ChatRoomService;
import com.example.airlineproject.service.MailService;
import com.example.airlineproject.service.UserService;
import com.example.airlineproject.util.FileUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final PersistentTokenRepository persistentTokenRepository;

    @GetMapping("/register")
    private String registration(@RequestParam(value = "emailMsg", required = false) String emailMsg,
                                @RequestParam(value = "passwordErrorMsg", required = false) String passwordErrorMsg,
                                @RequestParam(value = "errorCode", required = false) String errorCode,
                                @RequestParam(value = "msg", required = false) String msg,
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
    public String registration(@ModelAttribute UserRegisterDto userRegisterDto,
                               @RequestParam("picture")
                               MultipartFile multipartFile) throws IOException {
        UserResponseDto byEmail = userService.findByEmail(userRegisterDto.getEmail());
        if (byEmail != null && byEmail.isActive()) {
            String emailMsg = "User with this email " + userRegisterDto.getEmail() + " already  exist";
            log.warn(emailMsg);
            return "redirect:/user/register?emailMsg=" + emailMsg;
        }
        if (userRegisterDto.getPassword().length() < 6) {
            String passwordErrorMsg = "Password cannot be shorter than 6 characters";
            log.warn(passwordErrorMsg);
            return "redirect:/user/register?passwordErrorMsg=" + passwordErrorMsg;
        }
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            String msg = "Password mismatch";
            log.warn(msg);
            return "redirect:/user/register?msg=" + msg;
        } else {
            User saveUser = userService.save(userRegisterDto, multipartFile);
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
                //ChatRoom-with-webSocket-VS start
                //in this case ChatRoom will create for communication with user and random Admin
                User randomAdmin = userService.findRandomAdmin();
                String chatId = chatRoomService.createChatId(user.getEmail(), randomAdmin.getEmail());
                log.info("ChatRoom already crated with {}", chatId);
                //ChatRoom-with-webSocket-VS end
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
        }else return "redirect:/user/login";
    }

    @PostMapping("/emailUpdate")
    public String emailUpdate(@AuthenticationPrincipal SpringUser springUser, @RequestParam("email") String email, @RequestParam("verificationCode") String verificationCode) {
        if (springUser.getUser() != null) {
            userService.processEmailUpdate(springUser, email, verificationCode);
            return "redirect:/user/profile";
        }else return "redirect:/user/login";
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findConnectedUsers(@AuthenticationPrincipal SpringUser springUser) {
        List<ChatRoom> byRecipientId = chatRoomRepository.findByRecipientId(springUser.getUser().getEmail());
        List<UserDto> users = new ArrayList<>();
        for (ChatRoom chatRoom : byRecipientId) {
            String senderId = chatRoom.getSenderId();
            users.add(UserDto.builder()
                    .nickName(senderId)
                    .build());
        }
        return ResponseEntity.ok(users);
    }
}
