package com.rahul.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rahul.model.Trainer;
import com.rahul.repository.TrainerRepository;

@Service
public class TrainerService {
    
    @Autowired
    private TrainerRepository trainerRepository;

    public void addTrainer(Trainer trainer){
        this.trainerRepository.save(trainer);
    }
    public List<Trainer> getallTrainer(){
      List<Trainer> trainer=this.trainerRepository.findAll();
      return trainer;
    }
    public Optional<Trainer> findByLinkedinProfile(String linkedin){
      return this.trainerRepository.findByLinkedin(linkedin);
    }

    public String saveImage(MultipartFile imageFile) {
    if (imageFile == null || imageFile.isEmpty()) {
        return null; // No image provided
    }
    String uploadDir = "D:/T2T/TeachToTech/src/main/resources/static/assets/img/trainers";
    String fileName = imageFile.getOriginalFilename();
    String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

    try {
        Path path = Paths.get(uploadDir, uniqueFileName);
        Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/assets/img/trainers" + uniqueFileName; // Relative path for frontend use
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to save image: " + e.getMessage());
    }
}

}
