package com.rahul.model;

import java.util.Optional;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Comment {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    // Establish the relationship to the Article
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
    
    // If users can make comments, link to User entity
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @CreationTimestamp
    private String timestamp;

}
