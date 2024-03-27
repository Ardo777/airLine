package com.example.airlineproject.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileUtil {

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    public byte[] getPicture ( String picName) throws IOException {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
           return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }

    public void deletePicture(String picName) {

        File filePath = new File(uploadDirectory+File.separator+picName);
        if (filePath.exists()){
            filePath.delete();
        }
    }
}
