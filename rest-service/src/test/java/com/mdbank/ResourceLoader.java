package com.mdbank;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ResourceLoader {
    public String getResourceAsString(String filePath) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String resultString;
        try {
            resultString = new String(Files.readAllBytes(Paths.get(classLoader.getResource(filePath).toURI())));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        return resultString;
    }

    public MockMultipartFile getResourceAsMultipartFileExcel(String filePath) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        MockMultipartFile firstFile = null;
        try {
            firstFile = new MockMultipartFile("excel", "excel.xlsx",
                    "application/vnd.ms-excel",
                    Files.readAllBytes(Paths.get(classLoader.getResource(filePath).toURI())));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return firstFile;
    }

    public MockMultipartFile getResourceAsMultipartFile(String filePath) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        MockMultipartFile firstFile = null;
        try {
            firstFile = new MockMultipartFile("photo", "photo.png", "images/png",
                    Files.readAllBytes(Paths.get(classLoader.getResource(filePath).toURI())));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return firstFile;
    }
}