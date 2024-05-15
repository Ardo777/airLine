package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.ChatRoom;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.repository.ChatRoomRepository;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.ChatRoomService;
import com.example.airlineproject.service.CompanyService;
import com.example.airlineproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final CompanyService companyService;
    private final UserRepository userRepository;

    @Override
    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return Optional.empty();
                });
    }

    @Override
    public String createChatId(String senderId, String recipientId) {
        String chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }

    @Override
    public ChatRoom getChatRoom(int companyId, User currentUser) {
        Company company = companyService.findById(companyId);
        List<User> allByCompany = userService.findAllByCompany(company);
        ChatRoom chatRoom = null;
        for (User user : allByCompany) {
            Optional<ChatRoom> bySenderIdAndRecipientId = chatRoomRepository.findBySenderIdAndRecipientId(currentUser.getEmail(), user.getEmail());
            if (bySenderIdAndRecipientId.isPresent()){
                chatRoom = bySenderIdAndRecipientId.get();
                break;
            }
        }
        if (chatRoom == null){
                Random random = new Random();
                User randomManager = allByCompany.get(random.nextInt(allByCompany.size()));
                createChatId(randomManager.getEmail(), currentUser.getEmail());
        }
        return chatRoom;
    }

    @Override
    public List<ChatRoom> findByRecipientId(String email) {
        return chatRoomRepository.findByRecipientId(email);
    }

}
