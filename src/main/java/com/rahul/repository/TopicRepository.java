package com.rahul.repository;

import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long>{

    List<Topic> findByCategory(com.rahul.model.Category category);
    Optional<Topic> findByName(String name);
    boolean existsByName(String name);

    
}
