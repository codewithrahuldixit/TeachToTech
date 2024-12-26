package com.rahul.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahul.enum_.CourseStatus;
import com.rahul.model.Course;
import com.rahul.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

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
        // Assuming the Course entity has a 'category' field that maps to the Category entity
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
}
