package com.rahul.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Assignment {

     @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long assignmentId;

    @OneToOne
    @JoinColumn(name = "topic_id", nullable = false)
    // @JsonIgnore
    private Topic topic;

    @ElementCollection
    private List<String> qa;
    
}
