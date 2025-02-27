package com.rahul.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.model.Assignment;
import com.rahul.model.Topic;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;
import com.rahul.repository.AssignmentRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AssignmentController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private NoteRepository noteRepository;

    @PostMapping("/save-assignment")
public ResponseEntity<String> saveAssignment(@RequestBody Assignment assignment) {
    System.out.println("Received Assignment: " + assignment);

    if (assignment.getTopic() == null) {
        return new ResponseEntity<>("Topic is required!", HttpStatus.BAD_REQUEST);
    }

    Topic topic = assignment.getTopic();

    if (topic.getTopicId() != null) {
        if (!topicRepository.existsById(topic.getTopicId())) {
            return new ResponseEntity<>("Topic not found!", HttpStatus.BAD_REQUEST);
        }
    } else {
        topic = topicRepository.save(topic);
    }

    assignment.setTopic(topic);
    assignmentRepository.save(assignment);
    return new ResponseEntity<>("Assignment saved successfully!", HttpStatus.CREATED);
}

    @CrossOrigin(origins = "*")
    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getTopics() {
        return ResponseEntity.ok(topicRepository.findAll());
    }
    

    
}
