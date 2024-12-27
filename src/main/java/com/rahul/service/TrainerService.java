package com.rahul.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
