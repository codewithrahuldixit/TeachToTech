package com.rahul.service;

import java.io.File;
import java.io.IOException;
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
    
        // Directory where the image will be stored on the server
        String uploadDir = "C:/Users/isp/Documents/ayushi_pagal/TeachToTech/src/main/resources/static/assets/img/team/";
        String fileName = imageFile.getOriginalFilename();
    
        try {
            // Create the directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
    
            // File object representing the destination
            File destinationFile = new File(uploadDir + fileName);
    
            // Save the file using transferTo()
            imageFile.transferTo(destinationFile);
    
            // Returning the saved file's relative path for frontend usage
            return "/assets/img/team/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }
   

}
