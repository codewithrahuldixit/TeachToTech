package com.rahul.model;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    private String trainerName;

    @NotNull
    @Size(max = 5000)
    private String trainerDescription;

    @NotNull
    private String trainerImage; 

    @NotNull
    private String trainerQualification;

    @NotNull
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
