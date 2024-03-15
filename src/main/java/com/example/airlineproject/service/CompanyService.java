package com.example.airlineproject.service;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CompanyService {
    String save(Company company, MultipartFile multipartFile) throws IOException;

    String findByEmail(String email);

    String registerCompany(User user, String name, String email, MultipartFile multipartFile) throws IOException;
}
