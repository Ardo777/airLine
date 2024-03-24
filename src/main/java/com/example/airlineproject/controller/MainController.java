package com.example.airlineproject.controller;

import com.example.airlineproject.util.FileUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MainController {

    private final FileUtil fileUtil;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
       return fileUtil.getPicture(picName);
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
