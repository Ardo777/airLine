package com.example.airlineproject.service;

import com.example.airlineproject.entity.Company;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CompanyService {
    Company save(Company company, MultipartFile multipartFile) throws IOException;
    Company byEmail(String email);
}
