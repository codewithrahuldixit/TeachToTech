package com.rahul.controller;

import java.util.List;
import java.util.Optional;

import com.rahul.model.*;
import com.rahul.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahul.service.CategoryService;
import com.rahul.service.CourseService;
import com.rahul.service.TrainerService;
import com.rahul.service.UserService;

@Controller
public class DemoController {

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;

	@Autowired
	private TrainerService trainerService;

	@Autowired
	private CategoryService categoryService;
    @Autowired
    private TopicRepository topicRepository;

	@GetMapping({ "/", "/index" })
	public String Home(Model model) {
		List<Category> categories = categoryService.getCategory();
		model.addAttribute("categories", categories);
		return "index";
	}
	@GetMapping("/addassignment")
	public String showAddAssignmentPage() {
		return "addAssignment";
	}
	@GetMapping("/addnotes")
	public String showAddNotesPage() {
		return "addNotes";
	}
	@GetMapping("/allnotes")
	public String showNotesPage(){
		return "Notes";
	}
	public String getMethodName(@RequestParam String param) {
		return new String();
	}
	
	public String showCourses(Model model) {
		List<Course> approvedCourses = courseService.getApprovedCourses();
		model.addAttribute("approvedCourses", approvedCourses);
		return "courses";
	}

	@GetMapping("/courses")
	public String showCourses(@RequestParam(value = "category", required = false) String category, Model model) {

		List<Category> categories = categoryService.getCategory();
		model.addAttribute("categories", categories);
		List<Course> courses;

		if (category != null && !category.isEmpty()) {
			courses = courseService.getCoursesByCategory(category);
		} else {
			courses = courseService.getApprovedCourses();
		}
		model.addAttribute("courses", courses);
		model.addAttribute("selectedCategory", category);
		return "courses";
	}

	@GetMapping("/pricing")
	public String pricing() {
		return "pricing";
	}

	@GetMapping("/about")
	public String about(Model model) {
		List<Category> categories = categoryService.getCategory();
		model.addAttribute("categories", categories);
		return "about";
	}
	// @GetMapping("/adminpreview")
	// public String adminReview() {
	// 	return "adminPreview";
	// }
	
	// @GetMapping("/discoverarticles")
	// public String Allarticle() {
	// 	return "discoverArticles";
	// }
	
	
	@GetMapping("/articlereview/preview")
	public String articleAdminPreview() {
		return "adminPreview";
	}


	@GetMapping("/contact")
	public String contact(Model model) {
		List<Category> categories = categoryService.getCategory();
		model.addAttribute("categories", categories);
		return "contact";
	}

	@GetMapping("/course-details/{id}")
	public String getCourseDetails(@PathVariable Long id, Model model) {
		Course course = courseService.findCourseById(id);
		List<Category> categories = categoryService.getCategory();
		model.addAttribute("categories", categories);

		model.addAttribute("course", course);

		return "course-details";
	}

	@GetMapping("/api/users/register")
	public String register() {
		return "RegistrationForm";

	}

	@GetMapping("/trainer-forms")
	public String trainerget() {
		return "";
	}

	@GetMapping("/api/users/login")
	public String getlogin() {
		return "LoginPage";

	}

	@GetMapping("/api/trainer/add")
	public String getMethodName() {
		return "AddTrainer";
	}

	@GetMapping("/trainers")
	public String getAllTrainers(Model model) {
		List<Category> categories = categoryService.getCategory();
		model.addAttribute("categories", categories);
		List<Trainer> trainers = trainerService.getallTrainer();
		model.addAttribute("trainers", trainers);
		return "trainers";
	}

	@GetMapping("/api/admin/register")
	public String addAdmin() {
		return "RegistrationForm";
	}

	@GetMapping("api/courses/edit/{id}")
	public String getCourseEditPage(@PathVariable Long id, Model model) throws JsonProcessingException {
		Course course = courseService.getCourseById(id);
		Course courseObject = this.courseService.convertObjectToJsonAndBack(course);
		model.addAttribute("course", courseObject);
		return "editCourse";
	}

	@GetMapping("api/topic/edit/{id}")
	public String getTopicEditPage(@PathVariable Long id, Model model) throws JsonProcessingException {
		Topic topic = topicRepository.findById(id).get();
		List<Topic> topicList = topicRepository.findAll();
		List<Category> categoryList = categoryService.getCategory();
		model.addAttribute("topicList", topicList);
		model.addAttribute("topic", topic);
		model.addAttribute("categoryList", categoryList);
		return "editNotes";
	}

	@GetMapping("api/trainer/edit/{id}")
	public String getTrainerEditPage(@PathVariable Long id, Model model) throws JsonProcessingException {
		Trainer trainer = this.trainerService.getByTrainerId(id);
		Trainer trainerObject = this.trainerService.convertObjectToJsonAndBack(trainer);
		model.addAttribute("trainer", trainerObject);
		return "editTrainer";
	}

	@GetMapping("api/users/update-password/{email}")
	public String forgotpassword(@PathVariable String email, Model model) {
		model.addAttribute("email", email);
		return "forgotPassword";
	}

	@GetMapping("/api/users/allusers")
	public String getAllUsers(Model model) {
		List<Users> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "allusers";
	}

	@GetMapping("/api/users/allusers/update/{email}")
	public String getUser(@PathVariable String email, Model model) {
		Optional<Users> optionalUser = this.userService.findByEmail(email);
		if (optionalUser.isPresent()) {
			Users user = optionalUser.get();
			model.addAttribute("user", user);
			return "updateuser";
		} else {
			model.addAttribute("error", "User not found");
			return "error";
		}
	}

	@GetMapping("/editAssignment/{assignmentId}")
	public String editAssignment(@PathVariable Long assignmentId) {
		return "editAssignment";
	}
	
}
