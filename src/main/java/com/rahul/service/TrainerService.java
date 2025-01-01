package com.rahul.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.model.Course;
import com.rahul.model.Trainer;
import com.rahul.repository.TrainerRepository;

@Service
public class TrainerService {
    
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
    
        // Directory where the image will be stored on the server
        String uploadDir = "D:/T2T/TeachToTech/src/main/resources/static/assets/img/team/";
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
        Optional<Trainer> duplicateTrainer =this.trainerRepository.findByLinkedin(existingTrainer.getLinkedin());

        if (duplicateTrainer.isPresent() && !duplicateTrainer.get().getTrainerId().equals(trainerId)) {
            throw new Exception("A course with the same name and instructor already exists.");
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
   public void deleteById(Long trainerId){
    this.trainerRepository.deleteById(trainerId);
   }

}
