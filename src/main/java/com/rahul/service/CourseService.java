package com.rahul.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.enum_.CourseStatus;
import com.rahul.model.Course;
import com.rahul.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private  ObjectMapper objectMapper;

     public void saveCourse(Course course) {
        course.setStatus(CourseStatus.APPROVED);
        this.courseRepository.save(course);
    }
    public void approveCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setStatus(CourseStatus.APPROVED);
        courseRepository.save(course);
    }

    public void rejectCourse(Long id, String comment) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setStatus(CourseStatus.REJECTED);
        course.setRejectionComment(comment); // Set rejection reason
        courseRepository.save(course);
    }

    public List<Course> getPendingCourse() {
       return this.courseRepository.findByStatus(CourseStatus.APPROVED);
    }
    
    public List<Course> getApprovedCourses() {
        return this.courseRepository.findByStatus(CourseStatus.APPROVED);
    }
    public List<Course> getRejectedCourses() {
        return this.courseRepository.findByStatus(CourseStatus.REJECTED);
    }
    //change done by ayushi
    public List<Course> getCoursesByCategory(String categoryName) {
        return courseRepository.findByCategoryName(categoryName);
    }
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    public Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
    }   
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Optional<Course> findByCourseName(String courseName) {
        return this.courseRepository.findByCourseName(courseName);
    }
    public Optional<Course> findByPrice(Double price) {
        return this.courseRepository.findByPrice(price);
    }
    public String convertObjectToJsonString(Course course) throws JsonProcessingException {
        return objectMapper.writeValueAsString(course);
    }

    // Convert JSON string back to Course object
    public Course convertJsonToModel(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Course.class);
    }

    // Combined example: Convert object to JSON and back to object
    public Course convertObjectToJsonAndBack(Course course) throws JsonProcessingException {
        String json = convertObjectToJsonString(course); // Convert object to JSON
        return convertJsonToModel(json); // Convert JSON back to object
    }

    public String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null; // No image provided
        }
    
        // Directory where the image will be stored on the server
        String uploadDir = "C:/Users/isp/Documents/ayushi_pagal/TeachToTech/src/main/resources/static/assets/img/";
        String fileName = imageFile.getOriginalFilename();
    
        try {
            // Create the directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
    
            // File object representing the destination
            File destinationFile = new File(uploadDir + fileName);
    
            // Save the file using transferTo()
            imageFile.transferTo(destinationFile);
    
            // Returning the saved file's relative path for frontend usage
            return "/assets/img/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }
    public Course updateCourseWithImage(Long courseId, Course updatedCourse, MultipartFile imageFile) throws Exception {
        Course existingCourse = courseRepository.findById(courseId)
            .orElseThrow(() -> new Exception("Course not found"));

        // Check for duplicate 
        Optional<Course> duplicateCourse = courseRepository.findByCourseNameAndPrice(
            updatedCourse.getCourseName(), updatedCourse.getPrice()
        );

        if (duplicateCourse.isPresent() && !duplicateCourse.get().getId().equals(courseId)) {
            throw new Exception("A course with the same name and instructor already exists.");
        }
        
            String imagePath = saveImage(imageFile);
            existingCourse.setImage(imagePath);  
        

        // Update other course fields
        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setCategory(updatedCourse.getCategory());
        existingCourse.setDuration(updatedCourse.getDuration());
        existingCourse.setModules(updatedCourse.getModules());
        existingCourse.setTrainers(updatedCourse.getTrainers());
        
        return courseRepository.save(existingCourse);
    }
}
