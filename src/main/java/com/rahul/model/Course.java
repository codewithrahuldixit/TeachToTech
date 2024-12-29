package com.rahul.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.enum_.CourseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;
   
    @Column(nullable = false)
    private String courseName;

    @Lob
    @Column(nullable=false,columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false) // Foreign key
    private Category category;

    @Column(nullable = false)
    private String image; // URL or file path to the image

    @Column(nullable = false)
    private Integer duration; // Duration of the course (e.g., in hours)

    @Column(nullable = false)
    private Double price; // Price of the course

    @Column(nullable = false)
    private String modules;

    @ManyToMany
    @JoinTable(
        name = "course_trainer", // Name of the join table
        joinColumns = @JoinColumn(name = "course_id"), // Foreign key to the Course table
        inverseJoinColumns = @JoinColumn(name = "trainer_id") // Foreign key to the Trainer table
    )
    private Set<Trainer> trainers;
    
    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.PENDING;
    
    @JsonIgnore
    private String rejectionComment="";
}
