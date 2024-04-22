package com.example.airlineproject.service.impl;  

import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
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



@Service
@RequiredArgsConstructor
@Slf4j
public class PlaneServiceImpl implements PlaneService {


    private final PlaneRepository planeRepository;
    private final FileUtil fileUtil;
    private final PlaneMapper planeMapper;
   
    @Override
    public List<PlanesResponseDto> allPlanesOfTheCompany(Company company) {
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
}
