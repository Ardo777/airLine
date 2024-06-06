package com.example.airlineproject.service;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CompanyService {

    String save(Company company, User user, MultipartFile picFile, MultipartFile certificateFile) throws IOException;

    String findByEmail(String email);

    String registerCompany(User user, String name, String email, MultipartFile picFile, MultipartFile certificateFile) throws IOException;

    List<Company> findByActive(boolean active);

    void accept(int id);

    void delete(int id);

    Company findByUser(User user);

    long count();

    Boolean starRatingSave(int companyId, int rating, User user);

    Boolean markExistByUserAndCompany(User user, Company company);

    Page<Company> findAllByRating(Pageable pageable);

    List<Company> getAllCompaniesByFilter(String keyword);

    Company findById(int id);




}
