package com.rahul.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import com.rahul.dto.LoginDto;
import com.rahul.model.Users;
import com.rahul.service.UserService;
import com.rahul.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
public String registerUser(@ModelAttribute Users user, @RequestParam String confirmPassword, Model model) {
  
    if (!user.getPassword().equals(confirmPassword)) {
        model.addAttribute("error", "Passwords do not match");
        return "RegistrationForm";
    }
    
    if (userService.findByEmail(user.getEmail()).isPresent()) {
        model.addAttribute("error", "Username is already taken");
        return "RegistrationForm";
    }
    
    userService.registerUser(user);
    return "redirect:/index"; 
}
    @GetMapping("/login")
    public String showLoginForm(Model model){
        model.addAttribute("LoginDto", new LoginDto());
        return "LoginPage";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginDto loginDto, Model model) {
       try{
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated()) {
           
            User user = (User) authentication.getPrincipal();
            String jwtToken = this.jwtUtil.generateToken(user);
            model.addAttribute("token", jwtToken);
            model.addAttribute("success", "You are logged in successfully!");
            return "redirect:/index";
        }
    
        }
        catch(BadCredentialsException e){
            model.addAttribute("error", "Invalid credentials. Please try again.");
        }
        model.addAttribute("LoginDto", loginDto);
        return "LoginPage";
        
    }

    @GetMapping("profile/{email}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users()); 
        return "RegistrationForm"; 
    }



}
