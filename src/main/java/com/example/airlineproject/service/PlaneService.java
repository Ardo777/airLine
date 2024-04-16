package com.example.airlineproject.service;

import com.example.airlineproject.dto.PlaneRequestDto;
import com.example.airlineproject.dto.PlaneResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Flight;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlaneService {
   List<PlaneResponseDto> getAllPlanesByCompany(Company company);

   PlaneResponseDto getPlane(int planeId, Company company);

   void changePlane(PlaneRequestDto planeRequestDto, MultipartFile multipartFile, Company company);
}
