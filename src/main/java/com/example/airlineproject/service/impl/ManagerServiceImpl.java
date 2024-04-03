package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.repository.OfficeRepository;
import com.example.airlineproject.repository.PlaneRepository;
import com.example.airlineproject.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    private final PlaneRepository planeRepository;
    private final OfficeRepository officeRepository;

    private void addPicture(MultipartFile multipartFile, Plane plane) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String planePic = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File picturesDir = new File(uploadDirectory);
            if (!picturesDir.exists()) {
                picturesDir.mkdirs();
            }
            String filePath = picturesDir.getAbsolutePath() + "/" + planePic;
            File file = new File(filePath);
            multipartFile.transferTo(file);
            plane.setPlanePic(planePic);
            log.info("Picture added to plane: " + plane.getModel());
        }
    }

    @Override
    public void saveAirPlane(Plane plane, MultipartFile multipartFile) throws IOException {
        addPicture(multipartFile, plane);
        planeRepository.save(plane);
        log.info("Plane saved: " + plane.getModel());
    }

    @Override
    public Plane createPlane(String model, double maxBaggage, int maxPassengers, MultipartFile multipartFile) {
        log.info("Creating plane with model: " + model);

        Plane plane = Plane.builder()
                .model(model)
                .maxBaggage(maxBaggage)
                .maxPassengers(maxPassengers)
                .build();

        return plane;
    }

    @Override
    public Boolean isPlaneExist(Plane plane) {
        boolean isPlaneExist = planeRepository.existsByModelAndMaxBaggageAndMaxPassengers(plane.getModel(), plane.getMaxBaggage(), plane.getMaxPassengers());
        if (isPlaneExist) {
            log.info("Plane exists: " + plane.getModel());
            return true;
        } else {
            log.info("Plane does not exist: " + plane.getModel());
            return false;
        }
    }

    @Override
    public Office saveOffice(Office office, User user) {
        office.setCompany(user.getCompany());
        return officeRepository.save(office);
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


}