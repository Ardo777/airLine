package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.QCompany;
import com.example.airlineproject.entity.StarRating;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.repository.CompanyRepository;
import com.example.airlineproject.repository.StarRatingRepository;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.CompanyService;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j

public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final StarRatingRepository starRatingRepository;
    private final EntityManager entityManager;

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
            log.info("File saved successfully: {}", picName);
        } else {
            log.warn("Multipart file is null or empty. No file saved.");
        }
    }


    @Override
    public String save(Company company, User user, MultipartFile multipartFile) throws IOException {
        if (companyRepository.findByUser(company.getUser()).isPresent()) {
            String errorMsg = String.format("The user %s %s already has a registered company",
                    company.getUser().getName(), company.getUser().getSurname());
            log.warn("Company registration failed: {}", errorMsg);
            return errorMsg;
        }
        if (companyRepository.findByName(company.getName()).isPresent()) {
            String errorMsg = String.format("A company with this %s name already exists", company.getName());
            log.warn("Company registration failed: {}", errorMsg);
            return errorMsg;
        }
        saveFile(multipartFile, company);
        user.setCompany(company);
        companyRepository.save(company);
        userRepository.save(user);
        log.info("Company saved successfully: {}", company);
        return null;
    }


    @Override
    public String registerCompany(User user, String name, String email, MultipartFile multipartFile) throws IOException {
        String msg = findByEmail(email);
        if (msg != null) {
            log.warn("Registration failed: {}", msg);
            return msg;
        }
        Company company = Company.builder()
                .user(user)
                .name(name)
                .email(email)
                .build();
        String result = save(company, user, multipartFile);
        if (result == null) {
            log.info("Company registered successfully: {}", company);
        } else {
            log.warn("Company registration failed: {}", result);
        }
        return result;
    }


    @Override
    public String findByEmail(String email) {
        Optional<Company> byEmail = companyRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return "Company with this email " + email + " already  exist";
        }
        return null;
    }

    @Override
    public List<Company> findByActive(boolean active) {
        List<Company> byActive = companyRepository.findByActive(active);
        if (byActive == null || byActive.isEmpty()) {
            log.info("No companies found with active status: {}", active);
            return null;
        }
        log.info("Found {} companies with active status: {}", byActive.size(), active);
        return byActive;
    }


    @Override
    public void accept(int id) {
        Optional<Company> byId = companyRepository.findById(id);
        if (byId.isPresent()) {
            Company company = byId.get();
            company.setActive(true);
            company.getUser().setRole(UserRole.valueOf("MANAGER"));
            companyRepository.save(company);
            company.getUser().setCompany(company);
            userRepository.save(company.getUser());
            log.info("User role changed to MANAGER for company with ID {}", id);
        } else {
            log.warn("Company with ID {} not found", id);
        }
    }


    @Override
    public void delete(int id) {
        Optional<Company> byId = companyRepository.findById(id);
        if (byId.isPresent()) {
            Company company = byId.get();
            company.getUser().setRole(UserRole.valueOf("USER"));
            String picName = company.getPicName();
            File file = new File(uploadDirectory + picName);
            if (file.exists()) {
                if (file.delete()) {
                    log.info("Picture {} is deleted", picName);
                } else {
                    log.warn("Failed to delete picture {}", picName);
                }
            } else {
                log.warn("Picture {} not found", picName);
            }
            companyRepository.deleteById(id);
            log.info("Company with ID {} is deleted", id);
        } else {
            log.warn("Company with ID {} not found", id);
        }
    }

    @Override
    public Company findByUser(User user) {
        Optional<Company> byUser = companyRepository.findByUser(user);
        return byUser.orElse(null);
    }

    @Override
    public long count() {
        return companyRepository.count();
    }


    @Override
    public Boolean starRatingSave(int companyId, int rating, User user) {
        Optional<Company> byId = companyRepository.findById(companyId);
        if (byId.isPresent()) {
            Company company = byId.get();
            if (markExistByUserAndCompany(user, company)) {
                return false;
            }
            StarRating starRating1 = StarRating.builder()
                    .rating(rating)
                    .user(user)
                    .company(company)
                    .build();
            starRatingRepository.save(starRating1);
            return true;
        }
        return false;
    }

    @Override
    public Boolean markExistByUserAndCompany(User user, Company company) {

        return starRatingRepository.existsByUserAndCompany(user, company);

    }

    @Override
    public Page<Company> findAllByRating(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.ASC, "rating");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Company> allByOrderByRatingDesc = companyRepository.findAllActiveCompanies(pageable);
        for (Company company : allByOrderByRatingDesc) {
            Integer averageRatingByCompanyId = starRatingRepository.getAverageRatingByCompanyId(company.getId());
            company.setRating(Objects.requireNonNullElse(averageRatingByCompanyId, 0));
        }
        return allByOrderByRatingDesc;
    }

    @Override
    public List<Company> getAllCompaniesByFilter(String keyword) {
        JPAQuery<Company> query = new JPAQuery<>(entityManager);
        QCompany qCompany = QCompany.company;
        JPAQueryBase<Company, JPAQuery<Company>> from = query.from(qCompany);
        List<Company> fetch;
        if (StringUtils.isNotBlank(keyword)){
            from.where(qCompany.name.contains(keyword));
        }
        fetch=query.fetch();
        return fetch;
    }


    @Override
    public Company findById(int id) {
        Optional<Company> byId = companyRepository.findById(id);
        return byId.orElse(null);
    }


}



