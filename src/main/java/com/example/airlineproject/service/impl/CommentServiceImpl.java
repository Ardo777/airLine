package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Comment;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.repository.CommentRepository;
import com.example.airlineproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;


    @Override
    public void createComment(Company company, User user, String comment, Date date) {
        Comment saveComment = Comment.builder()
                .user(user)
                .comment(comment)
                .company(company)
                .commentDate(date)
                .build();
        commentRepository.save(saveComment);
    }


    @Override
    public List<Comment> getAllCommentsByCompanyId(int companyId) {
        return commentRepository.findAllByCompanyIdOrderByCommentDateDesc(companyId);
    }
}
