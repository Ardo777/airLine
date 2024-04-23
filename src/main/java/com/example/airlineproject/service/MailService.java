package com.example.airlineproject.service;


import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.entity.User;

public interface MailService {

    void sendMail(UserRegisterDto userRegisterDto);


    void sendRecoveryMail(String email);

    void sendBirthdayMail(User user);


}
