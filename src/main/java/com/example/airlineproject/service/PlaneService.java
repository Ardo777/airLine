package com.example.airlineproject.service;

import com.example.airlineproject.dto.PlaneAddDto;

import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.security.SpringUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlaneService {

    Boolean isPlaneExist(PlaneAddDto planeAddDto, User user);

    void saveAirPlane(PlaneAddDto planeAddDto, MultipartFile multipartFile, SpringUser springUser) throws IOException;

    PlaneAddDto createPlane(PlaneAddDto planeAddDto, MultipartFile multipartFile);

    List<PlaneDto> getAllPlanesByCompany(Company company);

    PlaneDto getPlane(int planeId, Company company);

    void updatePlane(PlaneUpdateDto planeUpdateDto, MultipartFile multipartFile, Company company);

}
