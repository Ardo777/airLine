package com.example.airlineproject.service;


import com.example.airlineproject.entity.ChatRoom;
import com.example.airlineproject.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {
    Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists);

    String createChatId(String senderId, String recipientId);

    ChatRoom getChatRoom(int companyId, User currentUser);


    List<ChatRoom> findByRecipientId(String email);
}
