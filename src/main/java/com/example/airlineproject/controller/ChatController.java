package com.example.airlineproject.controller;

import com.example.airlineproject.entity.ChatMessage;
import com.example.airlineproject.entity.ChatNotification;
import com.example.airlineproject.entity.ChatRoom;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.ChatMessageService;
import com.example.airlineproject.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                              @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }


    @GetMapping("/messages")
    public String chatMessagePage() {
        return "chatMessage";
    }

    @GetMapping("/messages/{companyId}")
    public String createChatWithCompany(@PathVariable("companyId") int companyId,
                                        @AuthenticationPrincipal SpringUser springUser,
                                        ModelMap modelMap
    ) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(companyId, springUser.getUser());
        modelMap.addAttribute("selectedUser", chatRoom.getRecipientId());
        return "chatMessage";
    }


}
