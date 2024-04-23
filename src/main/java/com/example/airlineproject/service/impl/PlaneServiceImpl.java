package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.PlaneAddDto;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.PlaneMapper;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.PlaneService;
import com.example.airlineproject.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
public class PlaneServiceImpl implements PlaneService {


    private final PlaneRepository planeRepository;
    private final FileUtil fileUtil;
    private final PlaneMapper planeMapper;


    @Override
    public void saveAirPlane(PlaneAddDto planeAddDto, MultipartFile multipartFile, SpringUser springUser) throws IOException {
        Plane plane = planeMapper.mapToPlane(planeAddDto);
        String picName = fileUtil.saveFile(multipartFile);
        plane.setCompany(springUser.getUser().getCompany());
        plane.setPlanePic(picName);
        planeRepository.save(plane);
        log.info("Plane saved: {}", plane.getModel());
    }

    @Override
    public PlaneAddDto createPlane(PlaneAddDto planeAddDto, MultipartFile multipartFile) {
        log.info("Creating plane with model: {}", planeAddDto.getModel());

        return PlaneAddDto.builder()
                .model(planeAddDto.getModel())
                .maxBaggage(planeAddDto.getMaxBaggage())
                .countBusiness(planeAddDto.getCountBusiness())
                .countEconomy(planeAddDto.getCountEconomy())
                .countRow(planeAddDto.getCountRow())
                .build();
    }


    @Override
    public Boolean isPlaneExist(PlaneAddDto planeAddDto, User user) {
        boolean isPlaneExist = planeRepository.existsByModelAndMaxBaggageAndCountBusinessAndCountEconomyAndCompany(planeAddDto.getModel(), planeAddDto.getMaxBaggage(), planeAddDto.getCountBusiness(), planeAddDto.getCountEconomy(), user.getCompany());
        if (isPlaneExist) {
            log.info("Plane exists: {}", planeAddDto.getModel());
            return true;
        } else {
            log.info("Plane does not exist: {}", planeAddDto.getModel());
            return false;
        }
    }
}
