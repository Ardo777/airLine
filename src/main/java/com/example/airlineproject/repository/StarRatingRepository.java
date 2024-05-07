package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.StarRating;
import com.example.airlineproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRatingRepository extends JpaRepository<StarRating, Integer> {

    Boolean existsByUserAndCompany(User user, Company company);

    @Query(value = "SELECT AVG(rating) FROM star_rating WHERE company_id = :companyId", nativeQuery = true)
    Integer getAverageRatingByCompanyId(int companyId);

}
