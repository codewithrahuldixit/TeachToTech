package com.rahul.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.model.Course;
import com.rahul.service.CourseService;


@Controller
@RequestMapping("/api/courses")
public class CourseController {
	 
    @Autowired
    private CourseService courseService;

    @GetMapping("/addNew")
    public String getAdcourse(Model model){
        model.addAttribute("course", new Course());
        return "AddNewCourse"; 
    }
 
  @PostMapping("/add/pending")
  public ResponseEntity<String> addCourse(
    @RequestParam("payload") String courseDataJson,
    @RequestParam(value = "image", required = false) MultipartFile imageFile) {

    try {
        // Parse the JSON string to get course data
        ObjectMapper objectMapper = new ObjectMapper();
        Course course = objectMapper.readValue(courseDataJson, Course.class);

        // Handle the uploaded file (image)
        String imagePath = this.courseService.saveImage(imageFile);
        if (imagePath != null) {
            course.setImage(imagePath);
        }

        // Save the course in the database
        Optional<Course> course1 = this.courseService.findByCourseName(course.getCourseName());
        if (course1.isPresent()) {
            if (this.courseService.findByPrice(course1.get().getPrice()).isPresent()) {
                return ResponseEntity.badRequest().body("This course already exists");
            }
        }
        this.courseService.saveCourse(course);

        return ResponseEntity.ok("Course added successfully");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error adding course: " + e.getMessage());
    }
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

    @GetMapping("/get/pending")
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

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(
    @PathVariable Long courseId,
    @RequestParam("updatedCourses") String updatedCourseJson,
    @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

    // Use the courseId from the URL
    try {
        // Parse the JSON string to get course data
        ObjectMapper objectMapper = new ObjectMapper();
        Course updatedCourse = objectMapper.readValue(updatedCourseJson, Course.class);
        Course updated = courseService.updateCourseWithImage(courseId, updatedCourse, imageFile);
        return ResponseEntity.ok(updated);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}


    //change done by ayushi 
    @GetMapping("/details/{id}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
   

}
