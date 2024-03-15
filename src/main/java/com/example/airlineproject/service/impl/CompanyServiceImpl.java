package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
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

    private void saveFile(MultipartFile multipartFile, Company company) throws IOException {
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
    }

    @Override
    public String save(Company company, MultipartFile multipartFile) throws IOException {
        if (companyRepository.findByUser(company.getUser()).isPresent() || companyRepository.findByName(company.getName()).isPresent()) {
            return String.format("this user %s %s already has his own company or this company name is already taken",
                    company.getUser().getName(), company.getUser().getSurname());
        }
        saveFile(multipartFile, company);
        companyRepository.save(company);
        return null;
    }

    @Override
    public String registerCompany(User user, String name, String email, MultipartFile multipartFile) throws IOException {
        String msg = findByEmail(email);
        if (msg != null) {
            return msg;
        }
        Company company = Company.builder()
                .user(user)
                .name(name)
                .email(email)
                .build();
        saveFile(multipartFile, company);
        companyRepository.save(company);
        return null;
    }

    @Override
    public String findByEmail(String email) {
        Optional<Company> byEmail = companyRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return "Company with this email " + email + " already  exist";
        }
        return null;
    }
}
