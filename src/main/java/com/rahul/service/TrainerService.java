package com.rahul.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.model.Category;
import com.rahul.model.Trainer;
import com.rahul.repository.TrainerRepository;


@Service
public class TrainerService {

    @Value("${file.trainer-upload-dir}")
    private String uploadDir;

      private static final Trainer DEFAULT_TRAINER = new Trainer(
            1L, // Static ID for the default trainer
            "Default Trainer", // Name
            "This is a default trainer added automatically.", // Description
            "default-image.png", // Image
            "Default Qualification", // Qualification
            "https://www.linkedin.com/in/default-trainer", // LinkedIn URL
            new HashSet<>(Collections.singleton(new Category(1L, "Default Category", null))), // Categories
            new HashSet<>() // Courses
    );
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private  ObjectMapper objectMapper;
    
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
        // Extract original file name and sanitize it
        String originalFileName = imageFile.getOriginalFilename();
        if (originalFileName == null) {
            throw new RuntimeException("File name cannot be null.");
        }
        String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
        try {
            // Ensure the upload directory ends with a separator
            if (!uploadDir.endsWith(File.separator)) {
                uploadDir += File.separator;
            }

            // Create the directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("Failed to create upload directory: " + uploadDir);
            }

            // Destination file path
            File destinationFile = new File(directory, sanitizedFileName);

            // Save the file
            imageFile.transferTo(destinationFile);

            // Return the relative path to the file
            return "/assets/img/team/"+ sanitizedFileName;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    public Trainer getByTrainerId(Long id) {
       return this.trainerRepository.findByTrainerId(id).orElse(null);
    }

    public String convertObjectToJsonString(Trainer trainer) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(trainer);
    }

    // Convert JSON string back to Course object
    public Trainer convertJsonToModel(String json) throws JsonProcessingException {
        return this.objectMapper.readValue(json, Trainer.class);
    }

    // Combined example: Convert object to JSON and back to object
    public Trainer convertObjectToJsonAndBack(Trainer trainer) throws JsonProcessingException {
        String json = convertObjectToJsonString(trainer); // Convert object to JSON
        return convertJsonToModel(json); // Convert JSON back to object
    }
    public Trainer updateTrainerWithImage(Long trainerId, Trainer updatedTrainer, MultipartFile imageFile) throws Exception {
        Trainer existingTrainer = this.trainerRepository.findById(trainerId)
            .orElseThrow(() -> new Exception("Trainer not found"));

        // Check for duplicate 
        Optional<Trainer> duplicateTrainer =this.findByLinkedinProfile(existingTrainer.getLinkedin());

        if (duplicateTrainer.isPresent() && !duplicateTrainer.get().getTrainerId().equals(trainerId)) {
            throw new Exception("A Trainer with the same name already exists.");
        }
        
            String imagePath = this.saveImage(imageFile);
            existingTrainer.setTrainerImage(imagePath);

            existingTrainer.setTrainerName(updatedTrainer.getTrainerName());
            existingTrainer.setTrainerDescription(updatedTrainer.getTrainerDescription());
            existingTrainer.setTrainerQualification(updatedTrainer.getTrainerQualification());
            existingTrainer.setLinkedin(updatedTrainer.getLinkedin());
            existingTrainer.setCategories(updatedTrainer.getCategories());
        
        return this.trainerRepository.save(existingTrainer);
    }
    public void deleteById(Long trainerId) {
        this.trainerRepository.deleteById(trainerId);
        this.ensureDefaultTrainer();
    }
    public void ensureDefaultTrainer() {
        if (trainerRepository.count() == 0) {
            trainerRepository.save(DEFAULT_TRAINER);
        }
    }

}
