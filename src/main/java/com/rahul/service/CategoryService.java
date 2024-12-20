package com.rahul.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahul.model.Category;
import com.rahul.repository.CategoryRepository;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public void addCategory(Category category){
       this.categoryRepository.save(category);
    }
    public List<Category> getCategory(){
      List<Category> category= this.categoryRepository.findAll();
            return category;
    }
    
}
