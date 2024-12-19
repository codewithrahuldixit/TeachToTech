package com.rahul.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/yash")
public class YashController {
    @GetMapping("/get")
    public ResponseEntity<?> get(){
          return ResponseEntity.ok("yash you got it");
    }
}
