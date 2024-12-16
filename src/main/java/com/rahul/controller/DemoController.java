package com.rahul.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
	
	@GetMapping({"/","/index"})
	public String Home() {
		return "index";		
	}
	@GetMapping("/courses")
	public String courses() {
		return "courses";		
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

	
}
