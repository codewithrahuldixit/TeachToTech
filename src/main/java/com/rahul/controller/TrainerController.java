package com.rahul.controller;

import java.util.List;

import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.model.Course;
import com.rahul.model.Trainer;
import com.rahul.service.TrainerService;

@RestController
@RequestMapping("api/trainer")
public class TrainerController {
    
    @Autowired
    private TrainerService TrainerService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam("formData") String trainerDataJson,
    @RequestParam(value = "imageFile", required = false) MultipartFile imageFile)
    {    
         try {
        // Parse the JSON string to get course data
        ObjectMapper objectMapper = new ObjectMapper();
        Trainer trainer = objectMapper.readValue(trainerDataJson, Trainer.class);

        // Handle the uploaded file (image)
        String imagePath = this.TrainerService.saveImage(imageFile);
        if (imagePath != null) {
            trainer.setTrainerImage(imagePath);
        }
        if (TrainerService.findByLinkedinProfile(trainer.getLinkedin()).isPresent()) {
            return ResponseEntity.badRequest().body("Trainer is already added");
        }
        this.TrainerService.addTrainer(trainer);
        return ResponseEntity.ok("Trainer added successfully");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error adding course: " + e.getMessage());
    }
}
   
    @GetMapping("/get")
    public ResponseEntity<List<Trainer>> getAllTrainer(){
      List<Trainer> trainer=this.TrainerService.getallTrainer();
     
     return ResponseEntity.ok(trainer);
    }

    @PostMapping("/edit/{trainerId}")
    public ResponseEntity<?> update(@PathVariable Long trainerId,
    @RequestParam("formData") String trainerDataJson,
    @RequestParam(value = "imageFile", required = false) MultipartFile imageFile)
    {    
         try {
        // Parse the JSON string to get trainer data
        ObjectMapper objectMapper = new ObjectMapper();
        Trainer updatedTrainer = objectMapper.readValue(trainerDataJson, Trainer.class);
        Trainer updated=this.TrainerService.updateTrainerWithImage(trainerId,updatedTrainer,imageFile);
        return ResponseEntity.ok(updated);      
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
}
