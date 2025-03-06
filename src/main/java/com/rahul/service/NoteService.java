package com.rahul.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahul.model.Note;
import com.rahul.model.Topic;
import com.rahul.repository.NoteRepository;
import com.rahul.repository.TopicRepository;

@Service
public class NoteService {
     @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TopicRepository topicRepository;

    public Topic addTopic(String name) {
        Optional<Topic> existingTopic = topicRepository.findByName(name);
        return existingTopic.orElseGet(() -> topicRepository.save(new Topic(null, null, name, null, null)));
    }

    public Note addNote(Long topicId, String content) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        Note note = new Note();
        return noteRepository.save(note);
    }
}
