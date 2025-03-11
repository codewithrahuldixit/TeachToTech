package com.rahul.controller;

import com.rahul.service.NoteService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.*;

import com.rahul.model.Note;
import com.rahul.model.Topic;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;

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
            noteService.updateNote(noteId, note);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Note updated successfully!", HttpStatus.OK);
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
        
            return new ResponseEntity<>("nOTE DELETED SUCCESFULLY " , 
                                       HttpStatus.OK);
        }
    }
