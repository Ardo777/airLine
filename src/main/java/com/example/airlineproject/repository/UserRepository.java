package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE DAY(u.birthday) = DAY(CURRENT_DATE()) AND MONTH(u.birthday) = MONTH(CURRENT_DATE())")
    List<User> findUsersByBirthdayToday();

    long count();

    Optional<User> findRandomUserByRole(UserRole role);

    User findRandomUserByCompany(Company company);

    List<User> findAllByCompany(Company company);

    boolean existsByEmail(String email);

}
