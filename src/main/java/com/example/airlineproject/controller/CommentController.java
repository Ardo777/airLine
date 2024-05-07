package com.example.airlineproject.controller;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.CommentService;
import com.example.airlineproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final CompanyService companyService;

    @PostMapping("/addComment")
    public String addComment(
            @RequestParam("comment") String comment,
            @RequestParam("companyId") int id,
            @AuthenticationPrincipal SpringUser springUser
    ) {
        Company company = companyService.findById(id);
        if (!comment.isBlank()) {
            commentService.createComment(company, springUser.getUser(), comment, new Date());
        }
        return "redirect:/company/" + company.getId();
    }

}
