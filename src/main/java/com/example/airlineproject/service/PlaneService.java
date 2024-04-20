package com.example.airlineproject.service;

import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlaneService {

    Boolean isPlaneExist(Plane plane, User user);
    void saveAirPlane(Plane plane, MultipartFile multipartFile) throws IOException;

    Plane createPlane(String model, double maxBaggage, int countBusiness,int countEconomy,int countRow, MultipartFile multipartFile);
    List<PlaneDto> getAllPlanesByCompany(Company company);

    PlaneDto getPlane(int planeId, Company company);

    void updatePlane(PlaneUpdateDto planeUpdateDto, MultipartFile multipartFile, Company company);

}
