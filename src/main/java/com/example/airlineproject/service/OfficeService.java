package com.example.airlineproject.service;

import com.example.airlineproject.dto.OfficeChangeDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.User;

import java.util.List;

public interface OfficeService {

    void saveOffice(Office office, User user);

    Boolean isOfficeExist(Office office);

    Office findByCompany(Company company);

    String changeOffice(OfficeChangeDto officeChangeDto);

    List<Office> getAllOfficesByUser(User user);

    Office findById(int id);
}
