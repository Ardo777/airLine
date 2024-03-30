package com.example.airlineproject.repository;

import com.example.airlineproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    @Query("select count(*) from User u where u.isActive = ?1")
    int  totalUsersByActive(boolean isActive);
}
