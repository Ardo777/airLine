package com.example.airlineproject.service.impl;  

import com.example.airlineproject.dto.PlaneDto;
import com.example.airlineproject.dto.PlaneUpdateDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.PlaneMapper;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.service.PlaneService;
import com.example.airlineproject.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.airlineproject.dto.PlanesResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.mapper.PlaneMapper;
import java.util.List;
import java.io.IOException;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
@Slf4j
public class PlaneServiceImpl implements PlaneService {


    private final PlaneRepository planeRepository;
    private final FileUtil fileUtil;
    private final PlaneMapper planeMapper;



   
    @Override
    public List<PlaneDto> allPlanesOfTheCompany(Company company) {
        return planeMapper.map(planeRepository.findAllByCompany(company));
    }

    @Override
    public void saveAirPlane(Plane plane, MultipartFile multipartFile) throws IOException {
        String picName = fileUtil.saveFile(multipartFile);
        plane.setPlanePic(picName);
        planeRepository.save(plane);
        log.info("Plane saved: " + plane.getModel());
    }

    @Override
    public Plane createPlane(String model, double maxBaggage, int countBusiness,int countEconomy,int countRow, MultipartFile multipartFile) {
        log.info("Creating plane with model: " + model);

        return Plane.builder()
                .model(model)
                .maxBaggage(maxBaggage)
                .countBusiness(countBusiness)
                .countEconomy(countEconomy)
                .countRow(countRow)
                .build();
    }

    @Override
    public Boolean isPlaneExist(Plane plane,User user) {
        boolean isPlaneExist = planeRepository.existsByModelAndMaxBaggageAndCountBusinessAndCountEconomyAndCompany(plane.getModel(),plane.getMaxBaggage(),plane.getCountBusiness(),plane.getCountEconomy(),user.getCompany());
        if (isPlaneExist) {
            log.info("Plane exists: " + plane.getModel());
            return true;
        } else {
            log.info("Plane does not exist: " + plane.getModel());
            return false;
        }

    }
    @Override
    public List<PlaneDto> getAllPlanesByCompany(Company company) {
        log.info("Retrieving all planes for company: {}", company.getName());
        return planeMapper.map(planeRepository.findAllByCompany(company));
    }

    @Override
    public PlaneDto getPlane(int planeId, Company company) {
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
    public void  updatePlane(PlaneUpdateDto planeUpdateDto, MultipartFile multipartFile, Company company) {
        log.info("Changing plane details for company: {}", company.getName());
        Optional<Plane> plane = planeRepository.findByIdAndCompany(planeUpdateDto.getId(), company);
        if (plane.isEmpty()){
            log.error("Plane with ID {} not found for company: {}", planeUpdateDto.getId(), company.getName());
            throw new RuntimeException();
        }
        try {
            String picName = fileUtil.saveFile(multipartFile);
            if (picName != null){
                planeUpdateDto.setPlanePic(picName);
            }else {
                planeUpdateDto.setPlanePic(plane.get().getPlanePic());
            }

            planeUpdateDto.setCompany(company);
            planeRepository.save(planeMapper.map(planeUpdateDto));
            log.info("Plane details changed successfully for company: {}", company.getName());
        } catch (IOException e) {
            log.error("Error occurred while changing plane details for company: {}", company.getName(), e);
            throw new RuntimeException(e);
        }
    }

}
