package com.example.airlineproject.repository;

import com.example.airlineproject.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamMember,Integer> {
}
