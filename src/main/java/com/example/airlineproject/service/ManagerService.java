package com.example.airlineproject.service;

import com.example.airlineproject.entity.Office;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ManagerService {
    void saveAirPlane(Plane plane, MultipartFile multipartFile) throws IOException;

    Plane createPlane(String model, double maxBaggage, int maxPassengers, MultipartFile multipartFile);

    Boolean isPlaneExist(Plane plane);

    Office saveOffice(Office office,User user);

    Boolean isOfficeExist(Office office);
}
