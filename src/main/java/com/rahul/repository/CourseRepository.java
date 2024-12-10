package com.rahul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
