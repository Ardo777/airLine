package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.MailService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class UserSchedulerServiceImplTest {

    @Test
    void sendBirthdayGreetings() {
        UserRepository userRepository = mock(UserRepository.class);
        MailService mailService = mock(MailService.class);


        UserSchedulerServiceImpl userSchedulerService = new UserSchedulerServiceImpl(userRepository, mailService);

        User user1 = User.builder()
                .id(1)
                .name("John")
                .surname("Doe")
                .email("john@example.com")
                .password("password")
                .role(UserRole.USER)
                .isActive(true)
                .verificationCode("verificationCode")
                .picName("picName")
                .build();

        User user2 = User.builder()
                .id(2)
                .name("Alice")
                .surname("Smith")
                .email("alice@example.com")
                .password("password")
                .role(UserRole.USER)
                .isActive(true)
                .verificationCode("verificationCode")
                .picName("picName")
                .build();

        User user3 = User.builder()
                .id(3)
                .name("Bob")
                .surname("Brown")
                .email("bob@example.com")
                .password("password")
                .role(UserRole.USER)
                .isActive(true)
                .verificationCode("verificationCode")
                .picName("picName")
                .build();
        user2.setBirthday(LocalDate.now());
        List<User> users = Arrays.asList(user1, user2, user3);

        when(userRepository.findUsersByBirthdayToday()).thenReturn(Arrays.asList(user2));

        userSchedulerService.sendBirthdayGreetings();

        verify(mailService, times(1)).sendBirthdayMail(user2);
        verify(mailService, never()).sendBirthdayMail(user1);
        verify(mailService, never()).sendBirthdayMail(user3);
    }

}