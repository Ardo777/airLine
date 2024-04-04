package com.example.airlineproject.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class MainController {

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }


    @GetMapping("/hotel")
    public String hotelPage() {
        return "hotel";
    }

    @GetMapping("/news")
    public String newsPage() {
        return "news";
    }

    @GetMapping("/aboutUs")
    public String aboutUsPage() {
        return "aboutUs";
    }


    @GetMapping("/addCompany")
    public String addCompanyPage() {
        return "addCompany";
    }
}
