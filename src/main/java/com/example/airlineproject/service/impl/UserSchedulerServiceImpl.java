package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.MailService;
import com.example.airlineproject.service.UserSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserSchedulerServiceImpl implements UserSchedulerService {

    private final UserRepository userRepository;
    private final MailService mailService;


    @Scheduled(cron = "0 0 0 * * *")
    public void sendBirthdayGreetings() {
        List<User> users = userRepository.findUsersByBirthdayToday();
        users.forEach(mailService::sendBirthdayMail);
    }


}
