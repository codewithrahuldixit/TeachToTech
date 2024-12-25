package com.rahul.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rahul.model.Course;
import com.rahul.service.CourseService;


@Controller
public class DemoController {

	@Autowired
    private CourseService service;
    
	@Autowired
    private CourseService courseService;

	@GetMapping("/courses")
	public String showCourses(Model model) {
		List<Course> pendingCourses = courseService.getPendingCourse();
	    model.addAttribute("pendingCourses", pendingCourses);
	     return "courses"; // Name of the Thymeleaf template
	}
 
	@GetMapping({ "/", "/index" })
	public String Home() {
		return "index";
	}

	@GetMapping("/pricing")
	public String pricing() {
		return "pricing";
	}

	@GetMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/events")
	public String events() {
		return "events";
	}

	@GetMapping("/trainers")
	public String trainers() {
		return "trainers";
	}

	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}

	
	

	@GetMapping("/course-details")
    public String coursedetails() {
        return "course-details"; // This should resolve to course-details.html in templates
    }
	@GetMapping("/api/users/register")
	public String register(){
		return "RegistrationForm";
	}
	// @GetMapping("/trainer-forms")
	// public String trainerget(){
	// 	return "";
	// }

	
}
