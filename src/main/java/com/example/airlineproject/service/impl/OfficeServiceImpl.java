package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.OfficeChangeDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.OfficeMapper;
import com.example.airlineproject.repository.OfficeRepository;
import com.example.airlineproject.service.OfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
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
    public void changeOffice(OfficeChangeDto officeChangeDto) {
        Optional<Office> byId = officeRepository.findById(officeChangeDto.getId());
        if (byId.isPresent()) {
            Office office = byId.get();
            log.info("Changing office with ID {}", officeChangeDto.getId());
            log.info("Old values: country={}, city={}, street={}, workStartTime={}, workEndTime={}, phone={}",
                    office.getCountry().getName(), office.getCity().getName(), office.getStreet(), office.getWorkStartTime(), office.getWorkEndTime(), office.getPhone());
            Office office1 = officeMapper.mapToOffice(officeChangeDto);
            office1.setCompany(office.getCompany());
            officeRepository.save(office1);
            log.info("New values: country={}, city={}, street={}, workStartTime={}, workEndTime={}, phone={}",
                    office.getCountry().getName(), office.getCity().getName(), office.getStreet(), office.getWorkStartTime(), office.getWorkEndTime(), office.getPhone());
        }
    }

}
