package com.example.airlineproject.service;


import com.example.airlineproject.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

     ChatMessage save(ChatMessage chatMessage);

     List<ChatMessage> findChatMessages(String senderId, String recipientId);
}
