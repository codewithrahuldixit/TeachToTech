package com.rahul.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rahul.model.Article;
import com.rahul.model.Category;
import com.rahul.model.Users;
import com.rahul.repository.CategoryRepository;
import com.rahul.repository.UserRepository;
import com.rahul.service.ArticleService;
import com.rahul.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ArticleController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private UserRepository userrepo;

    @GetMapping("/articlereview")
    public String getAllArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articleReview";
    }

    @PostMapping("/save-content")
    public String submitarticle(   @RequestParam("username") String username, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Article article = (Article) session.getAttribute("previewarticle");

        logger.info("Received submission request for user: " + username);

        if (article == null) {
            redirectAttributes.addFlashAttribute("error", "No article to Submit");
            return "redirect:/articlewriting";
        }
       
        Optional<Users> optionalUser = userrepo.findByfirstName(username);
        if (optionalUser.isEmpty()) {
            logger.error("ERROR: User not found: " + username);
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/api/users/login";  // If user is not found, redirect to login page
        }
        Users author = optionalUser.get();
        article.setAuthor(author);
        article.setStatus("NOT-REVIEWED");
        articleService.saveArticle(article);
        session.removeAttribute("previewarticle");
        model.addAttribute("article", article);
        redirectAttributes.addFlashAttribute("message", "Article submitted successfully!");
        System.out.println("DEBUG: Success message added!");

        return "redirect:/success";

    }

    @GetMapping("/articles")
    public List<Article> getAllArticlesApi() {
        return articleService.getAllArticles();
    }

    @GetMapping("/articlewriting")
    public String showArticleForm(Model model) {
        List<Category> categories = categoryRepo.findAll();
        System.out.println(categories);
        model.addAttribute("categories", categories);
        return "articlewriting"; // Ensure this matches the actual HTML file name
    }

    @GetMapping("/success")
    public String success() {
        return "articleSuccess"; // Ensure this matches your Thymeleaf template
    }

    @PostMapping("/preview")
    public String previewArticle(@RequestParam String title,
            @RequestParam String content,
            @RequestParam Long categoryId, HttpSession session, Model model) {

        logger.info("DEBUG:/PREVIEW CALL WITH TITLE" + title);
        Category category = articleService.getCategoryById(categoryId);

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setCategory(category);

        session.setAttribute("previewarticle", article);
        model.addAttribute("article", article);
        System.out.println("Article set in session: " + article.getTitle()); // Debugging

        return "preview";
    }


    @GetMapping("/preview")
    public String showPreview(HttpSession session, Model model,
            @RequestParam(value = "message", required = false) String message) {
        logger.info("DEBUG: Entering /preview handler...");

        Article article = (Article) session.getAttribute("previewarticle");

        if (article == null) {
            logger.info("DEBUG: No article found in session!");
            return "redirect:/articlewriting";
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        logger.info("DEBUG: Article retrieved from session: " + article.getTitle());
        model.addAttribute("article", article);
        return "preview";
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<String> updateArticle(@PathVariable long id, @RequestBody Article article) {
        try {
            articleService.updateArticle(id, article);
            return ResponseEntity.ok("Article updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deletearticle(@PathVariable long id) {
        if (articleService.deleteArticle(id)) {
            return ResponseEntity.ok("Article deleted successfully.");
        }
        return ResponseEntity.ok("Article failed to delete.");
    }

   
@GetMapping("/discoverarticles")
public String showDiscoverArticles(
        @RequestParam(name = "categoryId", required = false) Long categoryId,
        @RequestParam(name = "articleId", required = false) Long articleId,
        Model model) {

    List<Category> categories = categoryService.getCategory();
    model.addAttribute("categories", categories);

    List<Article> articles = new ArrayList<>();
    Category selectedCategory = null;
    if (categoryId != null) {
        articles = articleService.getArticlesByCategory(categoryId);
        model.addAttribute("articles", articles);

        selectedCategory = categoryService.getCategoryById(categoryId);
        model.addAttribute("selectedCategory", selectedCategory);
    }

 
    Article selectedArticle = null;
    if (articleId != null) {
        selectedArticle = articleService.getArticleById(articleId);
        model.addAttribute("selectedArticle", selectedArticle);
    }

    return "discoverArticles";
}
@GetMapping("/articlesByCategory")
@ResponseBody
public List<Article> getArticlesByCategory(@RequestParam Long categoryId) {
    List<Article> articles = articleService.getArticlesByCategory(categoryId);
    
    if (articles == null || articles.isEmpty()) {
        logger.info("No articles found for categoryId: " + categoryId);
    } else {
    	logger.info("Articles found for categoryId: " + categoryId + " - " + articles.size());
    }
    
    return articles;
}

@GetMapping("/adminpreview")
public String adminPreviewArticle(@RequestParam Long articleid, Model model) {
    Article article=articleService.getArticleById(articleid);
    model.addAttribute("article", article);
    return "adminPreview";
}

@GetMapping("/approve")
public ResponseEntity<String> Approvestatus(@RequestParam Long articleid){
    Article article= articleService.getArticleById(articleid);
    article.setStatus("APPROVED");
    if(article.getStatus()!="APPROVED"){
        return ResponseEntity.ok("Article not Approved");
    }

    return ResponseEntity.ok("Approved Article");
}


}
