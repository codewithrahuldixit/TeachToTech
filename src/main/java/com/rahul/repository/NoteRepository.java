package com.rahul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Note;

@Repository
public interface NoteRepository extends JpaRepository< Note,Long> {
    
}
