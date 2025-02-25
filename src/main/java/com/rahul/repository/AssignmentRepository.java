package com.rahul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    
}
