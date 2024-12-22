package com.rahul.model;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    @Column
    private String trainerName;

    @Column
    private String trainerDescription;

    @Column
    private String trainerImage; 

    @Column
    private String trainerQualification;

    @Column
    private String linkedin; // LinkedIn profile URL of the trainer

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false) // Foreign key for category
    private Category category;

    @ManyToMany(mappedBy = "trainers")
    @JsonIgnore
    private Set<Course> courses;

}
