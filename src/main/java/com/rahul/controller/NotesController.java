package com.rahul.controller;

import com.rahul.service.NoteService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.*;

import com.rahul.model.Note;
import com.rahul.model.Topic;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class NotesController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NoteService noteService;

    @PostMapping("/savenote")
    public ResponseEntity<String> postMethodName(@RequestBody Note note) {
        if (note.getTopic() == null || !topicRepository.existsById(note.getTopic().getTopicId())) {
            return new ResponseEntity<>("Topic not found!", HttpStatus.BAD_REQUEST);
        }

        noteRepository.save(note);
        return new ResponseEntity<>("Note saved successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/addTopic")
    public ResponseEntity<Topic> addTopic(@RequestBody Topic topic) {
        if (topicRepository.existsByName(topic.getName())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Topic savedTopic = topicRepository.save(topic);
        return new ResponseEntity<>(savedTopic, HttpStatus.CREATED);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }


    @PutMapping("/update-note/{noteId}")
    public ResponseEntity<String> updateNote(@PathVariable("noteId") long noteId, @RequestBody Note note) {
        try {
            
            if (note.getTopic() == null || note.getTopic().getTopicId() == null) {
                return new ResponseEntity<>("❌ Error: Topic ID is missing!", HttpStatus.BAD_REQUEST);
            }
    
            noteService.updateNote(noteId, note);
            return new ResponseEntity<>("✅ Note updated successfully!", HttpStatus.OK);
    
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("❌ Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-note/{noteId}")
    @Transactional
    public ResponseEntity<?> deleteNote(@PathVariable("noteId") long noteId) {
        System.out.println("Received request to delete note with ID: " + noteId);
        Note note = noteRepository.findById(noteId)
                              .orElseThrow(() -> new RuntimeException("Note not found"));

    Topic topic = note.getTopic();
    if (topic != null) {
        topic.setNote(null); // Remove the reference from Topic
        topicRepository.save(topic);
    }

    noteRepository.delete(note);
        
            return new ResponseEntity<>("NOTE deleted successfully " , 
                                       HttpStatus.OK);
        }

        @GetMapping("/get-note/{noteId}")
        public ResponseEntity<Map<String, Object>> getNoteById(@PathVariable Long noteId) {
            Optional<Note> optionalNote = noteService.findNoteById(noteId);
            
            if (!optionalNote.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Note not found"));
            }
            
            Note note = optionalNote.get();
            Map<String, Object> response = new HashMap<>();
            
            // Add topic details (handle null topic case)
            if (note.getTopic() != null) {
                Map<String, Object> topicDetails = new HashMap<>();
                topicDetails.put("topicId", note.getTopic().getTopicId());
                topicDetails.put("name", note.getTopic().getName());
                response.put("topic", topicDetails);
            } else {
                response.put("topic", null); // Avoid frontend errors
            }
        
            // Split content into an array (assuming content is stored as a string)
            response.put("content", note.getContent());
        
            return ResponseEntity.ok(response);
        }
          
 }
