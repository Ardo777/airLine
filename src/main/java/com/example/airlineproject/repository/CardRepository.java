package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Card;
import com.example.airlineproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Card findByUser(User user);

    Card findByIdNumber(String number);

}
