package com.example.airlineproject.controller;

import com.example.airlineproject.dto.CompanyFilterDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.City;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.QCompany;
import com.example.airlineproject.service.CityService;
import com.example.airlineproject.service.UserService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AjaxController {

    private final CityService cityService;
    private final UserService userService;
    private final EntityManager entityManager;

    @GetMapping("/cities")
    public List<City> getCities(@RequestParam("countryId") int countryId) {
        return cityService.getCitiesByCountry(countryId);
    }

    @GetMapping("/filter")
    public List<UserResponseDto> getByFilter(@RequestParam("keyword") String keyword) {
        log.info("User searching success");
        return userService.getAllByFilter(keyword);
    }

    @GetMapping("/filter/company")
    public ResponseEntity<Page<Company>> getByFilter(@RequestParam("key") String key,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        CompanyFilterDto companyFilterDto = new CompanyFilterDto();
        companyFilterDto.setName(key);
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        BooleanExpression predicate = buildPredicate(companyFilterDto);
        Pageable pageable = PageRequest.of(page, size);
        List<Company> companies = queryFactory.selectFrom(QCompany.company)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.selectFrom(QCompany.company)
                .where(predicate)
                .fetchCount();
        Page<Company> companyPage = new PageImpl<>(companies, pageable, total);
        return ResponseEntity.ok(companyPage);
    }

    private BooleanExpression buildPredicate(CompanyFilterDto companyFilterDto) {
        QCompany qCompany = QCompany.company;
        BooleanExpression predicate = qCompany.isActive.isTrue();
        if (companyFilterDto.getName() != null && !companyFilterDto.getName().isEmpty()) {
            predicate = predicate.and(qCompany.name.containsIgnoreCase(companyFilterDto.getName()));
        }
        if (companyFilterDto.getEmail() != null && !companyFilterDto.getEmail().isEmpty()) {
            predicate = predicate.and(qCompany.email.containsIgnoreCase(companyFilterDto.getEmail()));
        }
        return predicate;
    }


}
