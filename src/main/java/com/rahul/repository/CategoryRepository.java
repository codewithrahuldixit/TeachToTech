package com.rahul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Category;

@Repository
public interface  CategoryRepository extends JpaRepository<Category,Long> {

    Category findByName(String name);
    
}
