package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByEmail(String email);

    Optional<Company> findByUser(User user);

    Optional<Company> findByName(String name);

    @Query("SELECT c FROM Company c WHERE c.isActive = :active")
    List<Company> findByActive(boolean active);

}
