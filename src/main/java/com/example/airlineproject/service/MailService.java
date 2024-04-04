package com.example.airlineproject.service;


import com.example.airlineproject.entity.User;

public interface MailService {

    void sendMail(User user);

    void sendBirthdayMail(User user);

}
