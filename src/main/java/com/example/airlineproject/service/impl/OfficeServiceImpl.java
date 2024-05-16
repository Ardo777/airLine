package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.OfficeChangeDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.OfficeMapper;
import com.example.airlineproject.repository.CompanyRepository;
import com.example.airlineproject.repository.OfficeRepository;
import com.example.airlineproject.service.OfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfficeServiceImpl implements OfficeService {
    @Override
    public Office findById(int id) {
        return officeRepository.findById(id).orElse(null);
    }

    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;
    private final OfficeMapper officeMapper;


    @Override
    public void saveOffice(Office office, User user) {
        office.setCompany(user.getCompany());
        officeRepository.save(office);
    }

    @Override
    public Boolean isOfficeExist(Office office) {
        boolean isOfficeExist = officeRepository.existsByCountryAndCityAndStreet(office.getCountry(), office.getCity(), office.getStreet());
        if (isOfficeExist) {
            log.info("Office already exists");
            return true;
        } else {
            log.info("Office does not exist ");
            return false;
        }
    }

    @Override
    public Office findByCompany(Company company) {
        return officeRepository.findByCompany(company);
    }

    @Override
    public String changeOffice(OfficeChangeDto officeChangeDto) {
        Optional<Office> byId = officeRepository.findById(officeChangeDto.getId());
        if (byId.isPresent()) {
            Office office = byId.get();
            log.info("Changing office with ID {}", officeChangeDto.getId());
            log.info("Old values: country={}, city={}, street={}, workStartTime={}, workEndTime={}, phone={}",
                    office.getCountry().getName(), office.getCity().getName(), office.getStreet(), office.getWorkStartTime(), office.getWorkEndTime(), office.getPhone());
            if (officeChangeDto.getCountry() != null) {
                office.setCountry(officeChangeDto.getCountry());
            }
            if (officeChangeDto.getCity() != null) {
                office.setCity(officeChangeDto.getCity());
            }
            if (officeChangeDto.getStreet() != null) {
                office.setStreet(officeChangeDto.getStreet());
            }
            if (officeChangeDto.getWorkStartTime() != null) {
                office.setWorkStartTime(officeChangeDto.getWorkStartTime());
            }
            if (officeChangeDto.getWorkEndTime() != null) {
                office.setWorkEndTime(officeChangeDto.getWorkEndTime());
            }
            if (officeChangeDto.getPhone() != null) {
                office.setPhone(officeChangeDto.getPhone());
            }
            officeRepository.save(office);
            log.info("New values: country={}, city={}, street={}, workStartTime={}, workEndTime={}, phone={}",
                    office.getCountry().getName(), office.getCity().getName(), office.getStreet(), office.getWorkStartTime(), office.getWorkEndTime(), office.getPhone());

        }
        return "Office with ID " + officeChangeDto.getId() + " has been changed";
    }

    @Override
    public List<Office> getAllOfficesByUser(User user) {
        Optional<Company> company = companyRepository.findByUser(user);
        return company.map(officeRepository::findAllByCompany).orElse(null);
    }

}
