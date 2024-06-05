package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Subscribe;
import com.example.airlineproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {
   @Query("SELECT s.company FROM Subscribe s WHERE s.user = ?1")
   List<Company> findAllCompaniesByUser(User user);}
