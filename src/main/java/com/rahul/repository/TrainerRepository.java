package com.rahul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Trainer;



@Repository
public interface  TrainerRepository extends JpaRepository<Trainer, Long> {
  Optional<Trainer> findByLinkedin(String linkedin);
  Optional<Trainer> findByTrainerId(Long id);
  
}
