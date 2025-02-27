package com.rahul.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ImageController {

    @GetMapping("/view-image")
    public ResponseEntity<Resource> viewImage(@RequestParam String path) {
        try {
            Path filePath = Paths.get(path).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_JPEG) // Adjust based on image type
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build(); // 🛠 Fixed: Ensure .build() is properly closed
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build(); // 🛠 Fixed: Ensure .build() is properly closed
        }
    }
}

