package com.rahul.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.rahul.dto.LoginDto;
import com.rahul.model.Users;
import com.rahul.service.UserService;
import com.rahul.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userService.findByContact(user.getContact()).isPresent()) {
            return ResponseEntity.badRequest().body("Contact is already taken");
        }
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
}
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        // Combine email and contact into one string (this could also be done by creating a custom identifier)
        String identifier = loginDto.getEmail() + "|" + loginDto.getContact();
        // Use UsernamePasswordAuthenticationToken with the combined identifier
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, null));
 
        if (authentication.isAuthenticated()) {
            Map<String, String> authResponse = new HashMap<>();
            User user = (User) authentication.getPrincipal();   
            String jwtToken = this.jwtUtil.generateToken(user);  
            authResponse.put("token", jwtToken);
            
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }
        throw new UsernameNotFoundException("Invalid credentials");
    }
    

    @GetMapping("profile/{email}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
 
    @GetMapping("/name")
        public String getCurrentUsername(@RequestHeader("Authorization") String authorizationHeader) {
     // Remove "Bearer " prefix from the token
        if(authorizationHeader==null|| !authorizationHeader.startsWith("Bearer ")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header is missing or invalid");
        }
        String token = authorizationHeader.substring(7);
        String firstName=this.userService.getUsersDetails(token);
        return firstName;
    }
    @PostMapping("/getrole")
    public ResponseEntity<String> getRole(@RequestHeader("Authorization") String authorizationHeader){
        if(authorizationHeader==null|| !authorizationHeader.startsWith("Bearer ")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header is missing or invalid");
        }
        String token = authorizationHeader.substring(7);
        String role=this.jwtUtil.getRoleFromToken(token);
        if (role == null || role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found in token");
        }
        return ResponseEntity.ok(role);
    }
}
