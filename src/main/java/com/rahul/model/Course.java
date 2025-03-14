package com.rahul.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rahul.enum_.CourseStatus;
import com.rahul.service.ListStringConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;
   
    @NotNull
    private String courseName;

     
    @Column(columnDefinition = "TEXT")
    @NotNull
    @Convert(converter = ListStringConverter.class)
    private List<String> description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false) // Foreign key
    private Category category;

    @NotNull
    private String image; // URL or file path to the image

    @NotNull
    private Integer duration; // Duration of the course (e.g., in hours)

    @NotNull
    private Double price; // Price of the course

    @NotNull
    private String modules;

    @ManyToMany()
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
