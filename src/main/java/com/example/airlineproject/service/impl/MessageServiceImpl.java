package com.example.airlineproject.service.impl;


import com.example.airlineproject.entity.Message;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.repository.MessageRepository;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getCurrentlyMessages(User from, int to) {
        log.info("Getting currently messages for user with ID:{} ", to);
        Optional<User> user = userRepository.findById(to);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found.", to);
            throw new RuntimeException();
        }
        updateSeenByToAndFrom(from, user.get());
        return messageRepository.getCommunicationMessages(from, user.get());
    }

    private void updateSeenByToAndFrom(User from, User to) {
        List<Message> allByFromAndToAndSeen = messageRepository.findAllByFromAndToAndSeen(to, from, false);
        for (Message message : allByFromAndToAndSeen) {
            message.setSeen(true);
        }
        messageRepository.saveAll(allByFromAndToAndSeen);
    }

    @Override
    public void save(int toId, String message, User user) {
        if (userRepository.findById(toId).isPresent()) {
            messageRepository.save(Message.builder()
                    .to(userRepository.findById(toId).get())
                    .from(user)
                    .message(message)
                    .messageDate(new Date())
                    .build());
        }
    }

    @Override
    public List<Message> findNotifications(User from, boolean seen) {
        return messageRepository.findAllByToAndSeen(from, seen);
    }

}
