package com.example.airlineproject.controller;

import com.example.airlineproject.dto.ChangePasswordDto;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private SpringUser springUser;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private UserService userService;

    @Mock
    User user = new User();

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        when(springUser.getUser()).thenReturn(
                new User("Alice", "Smith", "test@example.com", "somepassword", true)
        );
    }

    @Test
    void userProfilePage_WithAuthenticatedUser() {
        ModelMap modelMap = mock(ModelMap.class);
        String result = userController.userProfilePage(springUser, modelMap);
        assertEquals("userProfile", result);
    }

    @Test
    void userProfilePage_WithUnauthenticatedUser() {
        when(springUser.getUser()).thenReturn(null);
        ModelMap modelMap = mock(ModelMap.class);
        String result = userController.userProfilePage(springUser, modelMap);
        assertEquals("redirect:/user/login", result);
    }


    @Test
    void userProfile() throws IOException {
        lenient().when(springUser.getUser()).thenReturn(new User());
        User user = new User();
        String result = userController.userProfile(user, springUser, multipartFile);
        assertEquals("redirect:/", result);
        verify(userService, times(1)).update(user, springUser, multipartFile);
    }

    @Test
    void changePassword_Success() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        when(userService.changePassword(changePasswordDto, springUser)).thenReturn(true);
        String result = userController.changePassword(changePasswordDto, springUser);
        assertEquals("redirect:/user/profile", result);
        verify(userService, times(1)).changePassword(changePasswordDto, springUser);
    }

    @Test
    void changePassword_Failure() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        when(userService.changePassword(changePasswordDto, springUser)).thenReturn(false);
        String result = userController.changePassword(changePasswordDto, springUser);
        assertEquals("redirect:/user/profile", result);
        verify(userService, times(1)).changePassword(changePasswordDto, springUser);
    }

    @Test
    void emailUpdatePage_Success() {
        String email = "test@example.com";
        ModelMap modelMap = new ModelMap();
        String result = userController.emailUpdatePage(springUser, email, modelMap);
        assertEquals("userUpdateMail", result);
        assertEquals(email, modelMap.get("email"));
        verify(userService, times(1)).updateEmail(springUser, email);
    }

    @Test
    void emailUpdatePage_NullUser() {
        String email = null;
        ModelMap modelMap = new ModelMap();
        when(springUser.getUser()).thenReturn(null);
        String result = userController.emailUpdatePage(springUser, email, modelMap);
        assertEquals("redirect:/user/login", result);
        assertNull(modelMap.get("email"));
        verify(userService, never()).updateEmail(any(), any());
    }

    @Test
    void emailUpdate_Success() {
        String email = "test@example.com";
        String verificationCode = "123456";
        String result = userController.emailUpdate(springUser, email, verificationCode);
        verify(userService, times(1)).processEmailUpdate(springUser, email, verificationCode);
        assertEquals("redirect:/user/profile", result);
    }

    @Test
    void emailUpdate_NullUser() {
        String email = "test@example.com";
        String verificationCode = "123456";
        when(springUser.getUser()).thenReturn(null);
        String result = userController.emailUpdate(springUser, email, verificationCode);
        verify(userService, never()).processEmailUpdate(any(), any(), any());
        assertEquals("redirect:/user/login", result);
    }
}
