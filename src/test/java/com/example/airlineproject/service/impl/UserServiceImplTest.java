package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.ChangePasswordDto;
import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.mapper.UserMapper;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.MailService;
import com.example.airlineproject.util.FileUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
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
    private UserMapper userMapper;

    @Mock
    private EntityManager entityManager;

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

    @Test
    void testSave_NewUser() throws IOException {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");
        MultipartFile multipartFile = mock(MultipartFile.class);
        User user = new User();
        user.setEmail("test@example.com");
        user.setActive(true);

        when(userRepository.findByEmail(userRegisterDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.mapToUser(userRegisterDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userServiceImpl.save(userRegisterDto, multipartFile);

        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSave_ExistingInactiveUser() throws IOException {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");
        MultipartFile multipartFile = mock(MultipartFile.class);
        User inactiveUser = new User();
        inactiveUser.setEmail("test@example.com");
        inactiveUser.setActive(false);
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(userRegisterDto.getEmail())).thenReturn(Optional.of(inactiveUser));
        when(userMapper.mapToUser(userRegisterDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userServiceImpl.save(userRegisterDto, multipartFile);

        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).deleteById(inactiveUser.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByEmail_UserExists() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userServiceImpl.findByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserResponseDto result = userServiceImpl.findByEmail(email);

        assertNull(result);
    }

    @Test
    void testDeleteById_UserExists() {
        int id = 1;
        User user = new User();
        user.setId(id);
        user.setPicName("pic.jpg");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userServiceImpl.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
        verify(fileUtil, times(1)).deletePicture("pic.jpg");
    }

    @Test
    void testDeleteById_UserDoesNotExist() {
        int id = 1;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        userServiceImpl.deleteById(id);

        verify(userRepository, times(0)).deleteById(id);
        verify(fileUtil, times(0)).deletePicture(anyString());
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = new ArrayList<>();
        users.add(new User());
        Page<User> page = new PageImpl<>(users);

        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userServiceImpl.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testRecoveryPassword() {
        User user = new User();
        String newPassword = "newPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        boolean result = userServiceImpl.recoveryPassword(user, newPassword);

        assertTrue(result);
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }


}
