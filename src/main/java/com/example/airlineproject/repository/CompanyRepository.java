package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT c FROM Company c WHERE c.isActive = ?1")
    List<Company> findByActive(boolean active);

    @Query(value = "SELECT * FROM company  WHERE is_active = true", nativeQuery = true)
    Page<Company> findAllActiveCompanies(Pageable pageable);



}
