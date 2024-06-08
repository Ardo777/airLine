package com.example.airlineproject.service;

import com.example.airlineproject.entity.Card;
import com.example.airlineproject.entity.User;

public interface CardService {

    Card gatByUser(User user);

    boolean transfer(double size, User user, User currentUser);


    Card findByNumber(String number);
}
