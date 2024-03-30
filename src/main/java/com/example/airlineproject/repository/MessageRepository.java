package com.example.airlineproject.repository;


import com.example.airlineproject.entity.Message;
import com.example.airlineproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {


    @Query("SELECT u FROM Message u WHERE u.from = ?1 AND u.to = ?2 or u.to = ?1 and u.from = ?2 order by u.messageDate asc")
    List<Message> getCommunicationMessages(User from, User to);

    List<Message> findAllByFromAndToAndSeen(User from, User to, boolean seen);

    List<Message> findAllByToAndSeen(User from, boolean seen);



}
