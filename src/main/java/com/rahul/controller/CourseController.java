package com.rahul.controller;


import java.util.List;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rahul.model.Course;
import com.rahul.model.Users;
import com.rahul.service.CourseService;


@Controller
@RequestMapping("/api/courses")
public class CourseController {
	
    @Autowired
	private CourseService service;

    @Autowired
    private CourseService courseService;

    @GetMapping("/addNew")
    public String getAdcourse(Model model){
        model.addAttribute("course", new Course()); 
        return "AddNewCourse"; 
    }

    @PostMapping("/add/pending")
    public ResponseEntity<?> createdCourse(@RequestBody Course course) {
        this.courseService.saveCourse(course);
        return ResponseEntity.ok("Course added and pending approval");
    }

    @PostMapping("/add/approved")
    public ResponseEntity<?> approvedCourse(@RequestBody Course course) {
        this.courseService.approveCourse(course.getId());
        return ResponseEntity.ok("Course approved");
    }
    @PostMapping("/add/rejected")
    public ResponseEntity<?> rejectedCourse(@RequestBody Course course) {
        this.courseService.rejectCourse(course.getId(),course.getRejectionComment());
        return ResponseEntity.ok("Course rejected");
    }

    @GetMapping("get/pending")
    public ResponseEntity<List<Course>> getPendingCourses() {
       List<Course> course= this.courseService.getPendingCourse();
        return ResponseEntity.ok(course);
    }
    @GetMapping("get/approved")
    public ResponseEntity<List<Course>> getApprovedCourses() {
       List<Course> course= this.courseService.getApprovedCourses();
        return ResponseEntity.ok(course);
    }
    @GetMapping("get/rejected")
    public ResponseEntity<List<Course>> getRejectedCourses() {
       List<Course> course= this.courseService.getRejectedCourses();
        return ResponseEntity.ok(course);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
