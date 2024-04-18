package com.example.airlineproject.service;

import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PlaneService {

    Boolean isPlaneExist(Plane plane, User user);
    void saveAirPlane(Plane plane, MultipartFile multipartFile) throws IOException;

    Plane createPlane(String model, double maxBaggage, int countBusiness,int countEconomy,int countRow, MultipartFile multipartFile);


}
