package com.example.airlineproject.service;


import com.example.airlineproject.entity.User;

public interface MailService {

    void sendMail(User user);

    void sendMail(String subject, String text);

    void sendRecoveryMail(User user);

    void sendBirthdayMail(User user);

}
