package com.rahul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rahul.model.Article;
import com.rahul.model.Category;
import com.rahul.service.ArticleService;
import com.rahul.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;
    
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Category category){
    Category savedCategory = this.categoryService.addCategory(category);
    return ResponseEntity.ok(savedCategory);
}

    @GetMapping("/getallcategory")
    public ResponseEntity<List<Category>> get(){
        List<Category> category=this.categoryService.getCategory();
        return ResponseEntity.ok(category);
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
    Category category = categoryService.findByName(name);  // Assuming you have a service method that fetches category by name
    if (category != null) {
        return ResponseEntity.ok(category);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Category not found
    }
}
   @GetMapping("/get/{trainerId}")
   public ResponseEntity<?> getCategoryByTrainerId(@PathVariable Long trainerId){
     List<Category> category=this.categoryService.findByTrainer(trainerId);
     return ResponseEntity.ok(category);
   }

   


    
}
