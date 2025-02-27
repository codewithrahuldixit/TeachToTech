package com.rahul.controller;

import org.springframework.web.bind.annotation.RestController;
import com.rahul.model.Topic;
import com.rahul.model.Category;
import com.rahul.model.Note;
import com.rahul.repository.CategoryRepository;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TopicRepository topicRepository;

@PostMapping("/createTopic")
public ResponseEntity<Object> CreateTopic(@RequestBody Topic topic) {
   if(topic.getCategory()==null || !categoryRepository.existsById(topic.getCategory().getCategoryId())){
    return new ResponseEntity<>("Category not found!", HttpStatus.BAD_REQUEST);
   }
   topic.setNote(null);
   topic.setAssignment(null);
   Topic savedTopic = topicRepository.save(topic);
        return new ResponseEntity<>(savedTopic, HttpStatus.CREATED);
}

 @GetMapping("/{categoryId}/topics")
    public ResponseEntity<List<Topic>> getTopicsByCategory(@PathVariable Long categoryId) {
        Category category= categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Topic> topics = topicRepository.findByCategory(category);
        if (topics.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<Topic> getTopicDetails(@PathVariable Long topicId) {
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (topic.isPresent()) {
            return ResponseEntity.ok(topic.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getTopics")
    public ResponseEntity<List<Topic>> getTopics() {
       List<Topic> topics= topicRepository.findAll();
       if (topics.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.ok(topics);
    }
    



    
    
}
