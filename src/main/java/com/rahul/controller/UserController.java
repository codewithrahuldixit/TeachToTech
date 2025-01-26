package com.rahul.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import com.rahul.dto.UpdatePasswordRequest;
import com.rahul.model.Users;
import com.rahul.service.OtpService;
import com.rahul.service.UserService;
import com.rahul.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

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
    String username = null;

    // Determine whether to use email or mobile number
    if (loginDto.getEmail() != null && !loginDto.getEmail().isEmpty()) {
        username = loginDto.getEmail();
    } else if (loginDto.getContact() != null && !loginDto.getContact().isEmpty()) {
        username = loginDto.getContact();
    } else {
        throw new IllegalArgumentException("Either email or mobile number must be provided");
    }
    log.info("users start authentication process with "+username);
    // Authenticate using the provided identifier and password
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, loginDto.getPassword())
    );
    log.info("User authenticating....with "+username);
    if (authentication.isAuthenticated()) {
        log.info("user authenticated successfully");
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
@PostMapping("/update-password")
public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request){
    Optional<Users> user = this.userService.findByEmail(request.getEmail());

    if (user.isPresent()) {
        if (user.get().isOtpUsed()) {
            // Call the user service to update the password
            boolean isPasswordUpdated = userService.updatePassword(request.getEmail(), request.getNewPassword());

            if (isPasswordUpdated) {
                return ResponseEntity.ok("Password updated successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to update password. Please try again.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
        }
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }
}
}
