package com.rahul.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rahul.model.Note;
import com.rahul.model.Topic;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class NotesController{

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private NoteRepository noteRepository;
    
    @PostMapping("/savenote")
    public ResponseEntity<String> postMethodName(@RequestBody Note note) {
       if(note.getTopic()==null || !topicRepository.existsById(note.getTopic().getTopicId())){
             return new ResponseEntity<>("Topic not found!", HttpStatus.BAD_REQUEST);
       }
     
        
       noteRepository.save(note);
       return new ResponseEntity<>("Note saved successfully!", HttpStatus.CREATED);
    }



    

}