package com.rahul.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rahul.model.Assignment;
import com.rahul.model.Topic;
import com.rahul.repository.AssignmentRepository;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@RestController
public class AssignmentController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    public EntityManager entityManager;
    
        @PostMapping("/save-assignment")
        public ResponseEntity<String> saveAssignment(@RequestBody Assignment assignment) {
            logger.info("Received Assignment: {}", assignment);
    
            if (assignment.getTopic() == null || !topicRepository.existsById(assignment.getTopic().getTopicId())) {
                return new ResponseEntity<>("Topic not found for the assignment!", HttpStatus.BAD_REQUEST);
            }
    
            Topic topic = assignment.getTopic();
            assignment.setTopic(topic);
            assignmentRepository.save(assignment);
            return new ResponseEntity<>("Assignment saved successfully!", HttpStatus.CREATED);
        }
        @GetMapping("/get-assignment/{assignmentId}")
    public ResponseEntity<?> getAssignment(@PathVariable Long assignmentId) {
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        if (assignment.isPresent()) {
            Assignment existingAssignment = assignment.get();
    
            // Include topic details in the response
            Map<String, Object> response = new HashMap<>();
            response.put("assignmentId", existingAssignment.getAssignmentId());
            response.put("topicId", existingAssignment.getTopic().getTopicId());
            response.put("topicName", existingAssignment.getTopic().getName());  // Add topicName here
            response.put("qa", existingAssignment.getQa());
    
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
    }
    
     
  
    // @Transactional
    // @PutMapping("/update-assignment/{assignmentId}")
    // public ResponseEntity<String> updateAssignment(@PathVariable Long assignmentId, @RequestBody Assignment updatedAssignment) {
    //     Optional<Assignment> assignment= assignmentRepository.findById(assignmentId);
    //     if(assignment!=null){
    //         Topic topic=assignment.get().getTopic();
    //     if(assignmentRepository.existsById(assignmentId)){
    //         assignmentRepository.deleteById(assignmentId);
    //     }
    //     updatedAssignment.setAssignmentId(assignmentId);
    //     updatedAssignment.setTopic(topic);
    
        
    //     return new ResponseEntity<>("Assignment saved",HttpStatus.CREATED);
    // }
    @Transactional
@PutMapping("/update-assignment/{assignmentId}")
public ResponseEntity<String> updateAssignment(@PathVariable Long assignmentId, @RequestBody Assignment updatedAssignment) {
    Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
    
    if (assignmentOptional.isPresent()) {
        Assignment existingAssignment = assignmentOptional.get();
        Topic topic = existingAssignment.getTopic();
        
        // Set the ID and topic on the updated assignment
        updatedAssignment.setAssignmentId(assignmentId);
        updatedAssignment.setTopic(topic);
        
        // Save the updated assignment
        Assignment saved = assignmentRepository.save(updatedAssignment);
        
        return new ResponseEntity<>("Assignment updated successfully", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Assignment not found", HttpStatus.NOT_FOUND);
    }
}

    
    
    // Delete assignment
    @DeleteMapping("/delete-assignment/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long assignmentId) {
        if (assignmentRepository.existsById(assignmentId)) {
            assignmentRepository.deleteById(assignmentId);
            return new ResponseEntity<>("Assignment deleted successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Assignment not found!", HttpStatus.NOT_FOUND);
    }


}
