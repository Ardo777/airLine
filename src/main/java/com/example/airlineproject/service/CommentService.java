package com.example.airlineproject.service;

import com.example.airlineproject.entity.Comment;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;

import java.util.Date;
import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsByCompanyId(int companyId);

    void createComment(Company company, User user, String comment, Date date);
}
