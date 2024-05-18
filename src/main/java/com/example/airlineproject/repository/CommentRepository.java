package com.example.airlineproject.repository;

import com.example.airlineproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByCompanyIdOrderByCommentDateDesc(int companyId);

}
