package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Card;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.repository.CardRepository;
import com.example.airlineproject.service.CardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public boolean transfer(double size, User user, User currentUser) {
        Card toCard = gatByUser(user);
        if (toCard == null) {
            throw new RuntimeException();//CardNotFoundException
        }
        Card fromCard = gatByUser(currentUser);
        double fromBalance = fromCard.getBalance();
        if (fromBalance < size) {
            return false;
        }
        fromCard.setBalance(fromBalance - size);
        toCard.setBalance(toCard.getBalance() + size);
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        return true;
    }
    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByIdNumber(number);
    }
    @Override
    public Card gatByUser(User user) {
        return cardRepository.findByUser(user);
    }
}
