package com.rahul.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.model.Trainer;
import com.rahul.service.TrainerService;

@RestController
@RequestMapping("api/trainer")
public class TrainerController {
    
    @Autowired
    private TrainerService TrainerService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Trainer trainer){
        if (TrainerService.findByLinkedinProfile(trainer.getLinkedin()).isPresent()) {
            return ResponseEntity.badRequest().body("Trainer is already added");
        }
        this.TrainerService.addTrainer(trainer);
        return ResponseEntity.ok("Trainer added successfully");
    }

    @GetMapping("/get")
    public ResponseEntity<List<Trainer>> getAllTrainer(){
      List<Trainer> trainer=this.TrainerService.getallTrainer();
     
     return ResponseEntity.ok(trainer);
    }
    


}
