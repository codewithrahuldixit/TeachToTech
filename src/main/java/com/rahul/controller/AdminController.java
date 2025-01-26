package com.rahul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.model.Users;
import com.rahul.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @PostMapping("/register")
    public ResponseEntity<?> add(@RequestBody Users user){
        if (adminService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (adminService.findByContact(user.getContact()).isPresent()) {
            return ResponseEntity.badRequest().body("Contact is already taken");
        }
       this.adminService.addAdmin(user);
           return ResponseEntity.ok().build();
       }
    
}
