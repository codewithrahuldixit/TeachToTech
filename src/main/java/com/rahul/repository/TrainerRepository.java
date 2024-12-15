package com.rahul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface  TrainerRepository extends JpaRepository<Object, Object> {

    
}
