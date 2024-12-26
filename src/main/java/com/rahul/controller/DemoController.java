package com.rahul.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rahul.model.Course;
import com.rahul.model.Trainer;
import com.rahul.service.CourseService;

import com.rahul.service.TrainerService;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class DemoController {

	@Autowired
    private CourseService service;
    
	@Autowired
    private CourseService courseService;


	@Autowired
    private TrainerService trainerService;

	@GetMapping({ "/", "/index" })
	public String Home() {
		return "index";
	}
	@GetMapping("/courses")
	public String showCourses(Model model) {
		
		List<Course> pendingCourses = courseService.getPendingCourse();
		List<Course> approvedCourses = courseService.getApprovedCourses();
		List<Course> rejectedCourses = courseService.getRejectedCourses();
	
		model.addAttribute("pendingCourses", pendingCourses);
		model.addAttribute("approvedCourses", approvedCourses);
		model.addAttribute("rejectedCourses", rejectedCourses);
	
		return "courses"; // Name of the Thymeleaf template

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

	// @GetMapping("/trainers")
	// public String trainers() {
	// 	return "trainers";
	// }

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
	@GetMapping("/trainer-forms")
	public String trainerget(){
		return "";
	}
	@GetMapping("/api/users/login")
	public String getlogin() {
		return "LoginPage";

	}
	
	@GetMapping("/api/trainer/addtrainer")
	public String getMethodName() {
		return "AddTrainer";
	}
	
	

	@GetMapping("/trainers")
    public String getAllTrainers(Model model) {
        List<Trainer> trainers = trainerService.getallTrainer();  // Fetch data from the service
        model.addAttribute("trainers", trainers);  // Add trainer data to the model
        return "trainers";  // Return the Thymeleaf template name
    }

	// @GetMapping("/trainer-forms")
	// public String trainerget(){
	// 	return "";
	// }


	
}
