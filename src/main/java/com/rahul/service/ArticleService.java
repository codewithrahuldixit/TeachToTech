package com.rahul.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahul.model.Article;
import com.rahul.model.Category;
import com.rahul.repository.ArticleRepository;
import com.rahul.repository.CategoryRepository;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public List<Article> getAllArticles() {
        return articleRepo.findAll();
        
    }

    public void saveArticle(Article article) {
        articleRepo.save(article);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepo.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Cannot fetch category"));
    }
    
       

    }
    
