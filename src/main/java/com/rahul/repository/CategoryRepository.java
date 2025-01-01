package com.rahul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rahul.model.Category;


@Repository
public interface  CategoryRepository extends JpaRepository<Category,Long> {

    Category findByName(String name);
    @Query("SELECT c FROM Category c JOIN c.trainers t WHERE t.trainerId = :trainerId")
    List<Category> findByTrainerId(@Param("trainerId") Long trainerId);
    
}
