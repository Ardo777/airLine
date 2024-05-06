package com.example.airlineproject.service;

import com.example.airlineproject.dto.OfficeChangeDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.User;

import java.util.Date;

public interface OfficeService {


    void saveOffice(Office office, User user);

    Boolean isOfficeExist(Office office);

    Office findByCompany(Company company);

    void changeOffice(OfficeChangeDto officeChangeDto);

}
