package com.rahul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.rahul.model.Category;
import com.rahul.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Category category){
     this.categoryService.addCategory(category);
     return ResponseEntity.ok("Category saved successfully");
    }
    @GetMapping("/getallcategory")
    public ResponseEntity<List<Category>> get(){
        List<Category> category=this.categoryService.getCategory();
        return ResponseEntity.ok(category);
    }
}
