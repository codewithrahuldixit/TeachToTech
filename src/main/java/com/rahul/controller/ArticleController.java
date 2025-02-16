package com.rahul.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rahul.model.Article;
import com.rahul.model.Category;
import com.rahul.repository.CategoryRepository;
import com.rahul.service.ArticleService;
import com.rahul.service.CategoryService;

import jakarta.servlet.http.HttpSession;



@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepo;

   
    @GetMapping("/articlereview")
    public String getAllArticles(Model model) {
         List<Article> articles= articleService.getAllArticles();
         model.addAttribute("articles", articles);
         return "articleReview";
    }
    
    // @PostMapping("/save-content")
    // public String postMethodName(@RequestParam String title, @RequestParam String content, @RequestParam Long categoryId, Model model) {
    //     Category articleCategory = categoryRepo.findById(categoryId)
    //     .orElseThrow(() -> new RuntimeException("Category not found"));
    //     Article article= new Article();
    //     article.setTitle(title);
    //     article.setContent(content);
    //     article.setCategory(articleCategory);

    //     articleService.saveArticle(article);

    //     model.addAttribute("article", article);

      
    //     return "redirect:/preview";
    // }

    @PostMapping("/save-content")
    public String submitarticle(HttpSession session,Model model, RedirectAttributes redirectAttributes){
        Article article= (Article) session.getAttribute("previewarticle");

        if(article==null){
            redirectAttributes.addFlashAttribute("error","No article to Submit");
            return "redirect:/write-article";
        }
        articleService.saveArticle(article);
        session.removeAttribute("previewarticle");
        model.addAttribute("article", article); 
        redirectAttributes.addFlashAttribute("message", "Article submitted successfully!");
        System.out.println("DEBUG: Success message added!");

        return "redirect:/preview";

    }
   
    

    @GetMapping("/articlewriting")
    public String showArticleForm(Model model) {
    List<Category> categories = categoryRepo.findAll();
    System.out.println(categories);
    model.addAttribute("categories", categories);
    return "articlewriting"; // Ensure this matches the actual HTML file name
}

 @GetMapping("/articles/category/{id}")
 public String getArticlesByCategory(@PathVariable Long id, Model model) {
    List<Article> articles = articleService.getArticlesByCategory(id); // Fetch articles by category
    Category category = categoryService.getCategoryById(id); // Fetch category details

    model.addAttribute("articles", articles);
    model.addAttribute("category", category);
    return "article-details"; // HTML template to display articles
}

    
    @PostMapping("/preview")
    public String previewArticle(@RequestParam String title, 
                                 @RequestParam String content, 
                                 @RequestParam Long categoryId, HttpSession session,Model model) {

      
        System.out.println("DEBUG:/PREVIEW CALL WITH TITLE"+ title);
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
    public String showPreview(HttpSession session, Model model,@RequestParam(value = "message", required = false) String message) {
    System.out.println("DEBUG: Entering /preview handler...");

    Article article = (Article) session.getAttribute("previewarticle");

    if (article == null) {
        System.out.println("DEBUG: No article found in session!");
        return "redirect:/write-article";
    }
    if (message != null) {  
        model.addAttribute("message", message);
    }
    System.out.println("DEBUG: Article retrieved from session: " + article.getTitle());
    model.addAttribute("article", article);
    return "preview";
}



    
}
