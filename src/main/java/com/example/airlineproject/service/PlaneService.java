package com.example.airlineproject.service;

import com.example.airlineproject.dto.PlaneAddDto;
import com.example.airlineproject.entity.Plane;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.security.SpringUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PlaneService {

    Boolean isPlaneExist(PlaneAddDto planeAddDto, User user);

    void saveAirPlane(PlaneAddDto planeAddDto, MultipartFile multipartFile, SpringUser springUser) throws IOException;

    PlaneAddDto createPlane(PlaneAddDto planeAddDto, MultipartFile multipartFile);


}
