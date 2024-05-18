package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.ChangePasswordDto;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.MailService;
import com.example.airlineproject.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private User user = new User("Alice", "Smith", "test@example.com", "somepassword", true);

    @Mock
    private SpringUser springUser;
    @Mock
    private FileUtil fileUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailService mailService;

    @BeforeEach
    void setUp() {
        lenient().when(springUser.getUser()).thenReturn(user);
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void update_ValidInput_SuccessfulUpdate() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        boolean result = userServiceImpl.update(user, springUser, multipartFile);
        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_InvalidInput_FailedUpdate() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        boolean result = userServiceImpl.update(null, null, multipartFile);
        assertFalse(result);
    }

    @Test
    void changePassword_MatchingPasswords_SuccessfulChange() {
        user.setPassword("oldPassword");
        user.setRole(UserRole.USER);
        SpringUser springUser = new SpringUser(user);
        when(userRepository.save(user)).thenReturn(user);
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setPassword("oldPassword");
        changePasswordDto.setNewPassword("newPassword");
        changePasswordDto.setConfirmNewPassword("newPassword");
        when(passwordEncoder.matches(eq("oldPassword"), anyString())).thenReturn(true);
        boolean result = userServiceImpl.changePassword(changePasswordDto, springUser);
        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_NotMatchingPasswords_FailedChange() {
        user.setRole(UserRole.USER);
        SpringUser springUser = new SpringUser(user);
        user.setPassword("oldPassword");
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setPassword("oldPassword");
        changePasswordDto.setNewPassword("newPassword");
        changePasswordDto.setConfirmNewPassword("differentPassword");
        boolean result = userServiceImpl.changePassword(changePasswordDto, springUser);
        assertFalse(result);
        verify(userRepository, never()).save(user);
    }

    @Test
    void changePassword_IncorrectCurrentPassword_FailedChange() {
        user.setRole(UserRole.USER);
        SpringUser springUser = new SpringUser(user);
        user.setPassword("oldPassword");
        boolean result = userServiceImpl.changePassword(new ChangePasswordDto("incorrectPassword", "newPassword", "newPassword"), springUser);
        assertFalse(result);
        verify(userRepository, never()).save(user);
    }

    @Test
    void updateEmail_ValidInput_SuccessfulUpdate() {
        user.setRole(UserRole.USER);
        SpringUser springUser = new SpringUser(user);
        String email = "test@example.com";
        userServiceImpl.updateEmail(springUser, email);
        assertFalse(user.isActive());
        assertNotNull(user.getVerificationCode());
        verify(mailService, times(1)).sendMail(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateEmail_NullSpringUser_NoUpdate() {
        String email = "test@example.com";
        userServiceImpl.updateEmail(null, email);
        verify(userRepository, never()).save(any());
    }

    @Test
    void processEmailUpdate_ValidVerificationCode_SuccessfulUpdate() {
        user.setRole(UserRole.USER);
        user.setVerificationCode("123456");
        SpringUser springUser = new SpringUser(user);
        String email = "test@example.com";
        String verificationCode = "123456";
        userServiceImpl.processEmailUpdate(springUser, email, verificationCode);
        assertTrue(user.isActive());
        assertEquals(email, user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void processEmailUpdate_InvalidVerificationCode_NoUpdate() {
        user.setRole(UserRole.USER);
        user.setVerificationCode("123456");
        SpringUser springUser = new SpringUser(user);
        String email = "test@example.com";
        String invalidVerificationCode = "654321";
        userServiceImpl.processEmailUpdate(springUser, email, invalidVerificationCode);
        assertFalse(user.isActive());
        assertEquals("test@example.com", user.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    void processEmailUpdate_NullSpringUser_NoUpdate() {
        String email = "test@example.com";
        String verificationCode = "123456";
        userServiceImpl.processEmailUpdate(null, email, verificationCode);
        verify(userRepository, never()).save(any());
    }
}
