package com.rahul.controller;

import com.rahul.service.NoteService;
import org.springframework.web.bind.annotation.*;

import com.rahul.model.Note;
import com.rahul.model.Topic;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;

import java.util.List;
import java.util.Map;

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


    @PutMapping("/api/topic/edit/{noteId}")
    public ResponseEntity<String> updateNote(
            @PathVariable("noteId") long noteId,
            @RequestBody Note note) {
        try {

            noteService.updateNote(noteId, note);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Note updated successfully!", HttpStatus.OK);
    }

    // @DeleteMapping("/delete-note/{noteId}")
    // public ResponseEntity<String> deleteNote(@PathVariable("noteId") long noteId) {
    //     if(noteRepository.existsById(noteId)){
    //         noteRepository.deleteById(noteId);
    //         return new ResponseEntity<>("Note deleted successfully!", HttpStatus.OK);

    //     }
    //     return new ResponseEntity<>("Note not found!", HttpStatus.NOT_FOUND);
    // }
    @DeleteMapping("/delete-note/{noteId}")
public ResponseEntity<String> deleteNote(@PathVariable("noteId") long noteId) {
    System.out.println("Received request to delete note with ID: " + noteId);
    
    if (noteRepository.existsById(noteId)) {
        System.out.println("Note exists. Deleting now...");
        noteRepository.deleteById(noteId);
        System.out.println("Note successfully deleted.");
        return new ResponseEntity<>("Note deleted successfully!", HttpStatus.OK);
    }

    System.out.println("Note not found in database!");
    return new ResponseEntity<>("Note not found!", HttpStatus.NOT_FOUND);
}

}