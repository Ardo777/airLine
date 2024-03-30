package com.example.airlineproject.service;



import com.example.airlineproject.entity.Message;
import com.example.airlineproject.entity.User;

import java.util.List;

public interface MessageService {
    List<Message> getCurrentlyMessages(User from, int to);
    void save(int toId,String message,User user);

    List<Message> findNotifications(User from,boolean seen);
}
