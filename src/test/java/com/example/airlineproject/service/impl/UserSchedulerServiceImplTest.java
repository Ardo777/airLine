package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.MailService;
import com.example.airlineproject.service.UserSchedulerService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserSchedulerServiceImplTest {

    @Test
    void sendBirthdayGreetings() { // Создаем заглушки для userRepository и mailService
        UserRepository userRepository = mock(UserRepository.class);
        MailService mailService = mock(MailService.class);

        // Создаем экземпляр BirthdayService с использованием заглушек
        UserSchedulerService userSchedulerService = new UserSchedulerServiceImpl(userRepository, mailService);

        // Подготовка данных для теста
        User user1 = new User(1, "John", "Doe", "john@example.com", "password", UserRole.USER, true, "verificationCode", "picName", null, null);
        User user2 = new User(2, "Alice", "Smith", "alice@example.com", "password", UserRole.USER, true, "verificationCode", "picName", null, null);
        User user3 = new User(3, "Bob", "Brown", "bob@example.com", "password", UserRole.USER, true, "verificationCode", "picName", null, null);
        user2.setDateBirthday(LocalDate.now()); // Установка сегодняшней даты рождения для Alice
        List<User> users = Arrays.asList(user1, user2, user3);

        // Задание поведения заглушки userRepository
        when(userRepository.findUsersByBirthdayToday()).thenReturn(Arrays.asList(user2)); // Возвращаем только user2

        // Вызов тестируемого метода
         userSchedulerService.sendBirthdayGreetings();

        // Проверка, что метод sendBirthdayMail был вызван только для user2
        verify(mailService, times(1)).sendBirthdayMail(user2);
        verify(mailService, never()).sendBirthdayMail(user1); // Проверяем, что для user1 не было вызова метода
        verify(mailService, never()).sendBirthdayMail(user3); // Проверяем, что для user3 не было вызова метода
    }

}