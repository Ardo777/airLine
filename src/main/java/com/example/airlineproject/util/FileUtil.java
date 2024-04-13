package com.example.airlineproject.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
@Slf4j
public class FileUtil {

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    public byte[] getPicture(String picName) throws IOException {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }

    public void deletePicture(String picName) {

        File filePath = new File(uploadDirectory + File.separator + picName);
        if (filePath.exists()) {
            filePath.delete();
        }
    }

    public String saveFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File picturesDir = new File(uploadDirectory);
            if (!picturesDir.exists()) {
                picturesDir.mkdirs();
            }
            String filePath = picturesDir.getAbsolutePath() + "/" + picName;
            File file = new File(filePath);
            multipartFile.transferTo(file);
            return picName;
        }
        return null;
    }

}
