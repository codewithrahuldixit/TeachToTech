package com.rahul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.model.Assignment;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AssignmentController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private NoteRepository noteRepository;

    @PostMapping("/save-assignment")
    public ResponseEntity<String> saveAssignment(@RequestBody Assignment assignment) {
        if(assignment.getTopic()==null || !topicRepository.existsById(assignment.getTopic().getTopicId()) ){
            return new ResponseEntity<>("Topic not found for the assignment!", HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>("Note saved successfully!", HttpStatus.CREATED);
    }
    
    
}
