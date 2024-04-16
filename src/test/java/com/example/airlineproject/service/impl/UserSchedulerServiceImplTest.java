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

        User user1 = new User(1, "John", "Doe", "john@example.com", "password", UserRole.USER, true, "verificationCode", "picName", null, null);
        User user2 = new User(2, "Alice", "Smith", "alice@example.com", "password", UserRole.USER, true, "verificationCode", "picName", null, null);
        User user3 = new User(3, "Bob", "Brown", "bob@example.com", "password", UserRole.USER, true, "verificationCode", "picName", null, null);
        user2.setDateBirthday(LocalDate.now());
        List<User> users = Arrays.asList(user1, user2, user3);


        when(userRepository.findUsersByBirthdayToday()).thenReturn(Arrays.asList(user2));


         userSchedulerService.sendBirthdayGreetings();


        verify(mailService, times(1)).sendBirthdayMail(user2);
        verify(mailService, never()).sendBirthdayMail(user1);
        verify(mailService, never()).sendBirthdayMail(user3);
    }

}