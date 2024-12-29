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
       this.adminService.addAdmin(user);
           return ResponseEntity.ok().build();
       
    }
    
}
