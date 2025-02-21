package com.rahul.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.enum_.CourseStatus;
import com.rahul.model.Course;
import com.rahul.repository.CourseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CourseService {

    @Value("${file.course-upload-dir}")
    private String uploadDir;
 
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private  ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
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
        // Extract original file name and sanitize it
        String originalFileName = imageFile.getOriginalFilename();
        if (originalFileName == null) {
            throw new RuntimeException("File name cannot be null.");
        }
        String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");

 
        try {
            // Ensure the upload directory ends with a separator
            if (!uploadDir.endsWith(File.separator)) {
                uploadDir += File.separator;
            }

            // Create the directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("Failed to create upload directory: " + uploadDir);
            }

            // Destination file path
            File destinationFile = new File(directory, sanitizedFileName);

            // Save the file
            imageFile.transferTo(destinationFile);

           // Return the relative path to the file (match your WebConfig handler)
            return "/assets/img/" + sanitizedFileName;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    @Transactional
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
        
            String imagePath = this.saveImage(imageFile);
            existingCourse.setImage(imagePath);
        // Update other course fields
        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setCategory(updatedCourse.getCategory());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setDuration(updatedCourse.getDuration());
        existingCourse.setModules(updatedCourse.getModules());
        existingCourse.setTrainers(updatedCourse.getTrainers());
        
        return courseRepository.save(existingCourse);
    }
}
