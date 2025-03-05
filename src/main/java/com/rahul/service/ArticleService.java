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
    public List<Article> getArticlesByCategory(Long categoryId) {
        return articleRepo.findByCategoryCategoryId(categoryId); // Query articles for a category
    }

    public boolean deleteArticle(long id) {
        if(articleRepo.existsById(id)){
            articleRepo.deleteById(id);
            return true;
        }
        return false;

    }
    public Article getArticleById(Long id) {
        return articleRepo.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
    }


    public Article updateArticle(long id, Article updatedArticle) {
        Article article = articleRepo.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        article.setTitle(updatedArticle.getTitle());
        article.setContent(updatedArticle.getContent());
        article.setCategory(updatedArticle.getCategory());
        return articleRepo.save(article);
    }
}
    
