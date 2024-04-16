package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.PlaneRequestDto;
import com.example.airlineproject.dto.PlaneResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.mapper.PlaneMapper;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.service.PlaneService;
import com.example.airlineproject.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final PlaneMapper planeMapper;
    private final FileUtil fileUtil;
    @Override
    public List<PlaneResponseDto> getAllPlanesByCompany(Company company) {
        log.info("Retrieving all planes for company: {}", company.getName());
        return planeMapper.map(planeRepository.findAllByCompany(company));
    }

    @Override
    public PlaneResponseDto getPlane(int planeId, Company company) {
        log.info("Retrieving plane with ID {} for company: {}", planeId, company.getName());
        Optional<Plane> plane = planeRepository.findByIdAndCompany(planeId, company);
        if (plane.isEmpty()){
            log.error("Plane with ID {} not found for company: {}", planeId, company.getName());
            throw new RuntimeException();
        }
        log.info("Retrieved plane with ID {} for company: {}", planeId, company.getName());
        return planeMapper.map(plane.get());
    }

    @Override
    public void changePlane(PlaneRequestDto planeRequestDto, MultipartFile multipartFile, Company company) {
        log.info("Changing plane details for company: {}", company.getName());
        Optional<Plane> plane = planeRepository.findByIdAndCompany(planeRequestDto.getId(), company);
        if (plane.isEmpty()){
            log.error("Plane with ID {} not found for company: {}", planeRequestDto.getId(), company.getName());
            throw new RuntimeException();
        }
        try {
            String picName = fileUtil.saveFile(multipartFile);
            if (picName != null){
                planeRequestDto.setPlanePic(picName);
            }else {
                planeRequestDto.setPlanePic(plane.get().getPlanePic());
            }
            planeRequestDto.setCompany(company);
            planeRepository.save(planeMapper.map(planeRequestDto));
            log.info("Plane details changed successfully for company: {}", company.getName());
        } catch (IOException e) {
            log.error("Error occurred while changing plane details for company: {}", company.getName(), e);
            throw new RuntimeException(e);
        }
    }


}
