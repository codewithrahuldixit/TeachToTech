package com.rahul.model;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @Column(nullable = false)
    private String trainerName;

    @Column(nullable = false)
    private String trainerDescription;

    @Column(nullable = false)
    private String trainerImage; 

    @Column(nullable = false)
    private String trainerQualification;

    @Column(nullable = false)
    private String linkedin; // LinkedIn profile URL of the trainer

    @ManyToMany
    @JoinTable(
        name = "trainer_categories",
        joinColumns = @JoinColumn(name = "trainer_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIncludeProperties({"categoryId","name"})
    private Set<Category> categories;
 
    @ManyToMany(mappedBy = "trainers")
    @JsonIgnore
    private Set<Course> courses;

}
