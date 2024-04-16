package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PlaneRepository extends JpaRepository<Plane, Integer> {
    List<Plane> findAllByCompany(Company company);


    Optional<Plane> findByIdAndCompany(int id, Company company);

    boolean existsByModelAndMaxBaggageAndCountBusinessAndCountEconomyAndCompany(String model, double maxBaggage, int countBusiness, int countEconomy, Company company);

  List<Plane> findAllByCompany(Company company);
}
