package com.rahul.controller;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.model.Assignment;
import com.rahul.model.Topic;
import com.rahul.repository.AssignmentRepository;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;


@RestController
public class AssignmentController {
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private NoteRepository noteRepository;

    @PostMapping("/save-assignment")
public ResponseEntity<String> saveAssignment(@RequestBody Assignment assignment) {
    logger.info("Received Assignment: " + assignment);


        if(assignment.getTopic()==null || !topicRepository.existsById(assignment.getTopic().getTopicId()) ){
            return new ResponseEntity<>("Topic not found for the assignment!", HttpStatus.BAD_REQUEST);
        }
    
    Topic topic = assignment.getTopic();
    assignment.setTopic(topic);
    assignmentRepository.save(assignment);
    return new ResponseEntity<>("Assignment saved successfully!", HttpStatus.CREATED);
}

    // @CrossOrigin(origins = "*")
    // @GetMapping("/topics")
    // public ResponseEntity<List<Topic>> getTopics() {
    //     return ResponseEntity.ok(topicRepository.findAll());
    // }
    

    
}
