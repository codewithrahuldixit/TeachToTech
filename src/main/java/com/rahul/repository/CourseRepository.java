package com.rahul.repository;

import java.util.List;
// import java.util.Optional;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.enum_.CourseStatus;
import com.rahul.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatus(CourseStatus approved);
    //change done by ayushi
    List<Course> findByCategoryName(String name);
    Optional<Course> findById(Long id);
}
