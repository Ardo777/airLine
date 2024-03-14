package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.repository.CompanyRepository;
import com.example.airlineproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;


    @Override
    public Company save(Company company, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File picturesDir = new File(uploadDirectory);
            if (!picturesDir.exists()) {
                picturesDir.mkdirs();
            }
            String filePath = picturesDir.getAbsolutePath() + "/" + picName;
            File file = new File(filePath);
            multipartFile.transferTo(file);
            company.setPicName(picName);
        }
        if (companyRepository.findByUser(company.getUser()).isPresent() || companyRepository.findByName(company.getName()).isPresent()) {
            return null;
        }
        return companyRepository.save(company);
    }

    @Override
    public Company byEmail(String email) {
        Optional<Company> byEmail = companyRepository.findByEmail(email);
        return byEmail.orElse(null);
    }
}
