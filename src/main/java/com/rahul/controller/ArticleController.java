package com.rahul.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articleReview";
    }

    @PostMapping("/save-content")
    public String submitarticle(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Article article = (Article) session.getAttribute("previewarticle");

        if (article == null) {
            redirectAttributes.addFlashAttribute("error", "No article to Submit");
            return "redirect:/articlewriting";
        }
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

        System.out.println("DEBUG:/PREVIEW CALL WITH TITLE" + title);
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
        System.out.println("DEBUG: Entering /preview handler...");

        Article article = (Article) session.getAttribute("previewarticle");

        if (article == null) {
            System.out.println("DEBUG: No article found in session!");
            return "redirect:/articlewriting";
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        System.out.println("DEBUG: Article retrieved from session: " + article.getTitle());
        model.addAttribute("article", article);
        return "preview";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deletearticle(@PathVariable long id) {
        if (articleService.deleteArticle(id)) {
            return ResponseEntity.ok("Article deleted successfully.");
        }
        return ResponseEntity.ok("Article failed to delete.");
    }

    // @GetMapping("/articletypes")
    // public String showArticleReviewPage(Model model) {
    // List<Category> categories = categoryService.getCategory();
    // Map<String, List<Article>> articlesByCategory = new HashMap<>();

    // for (Category category : categories) {
    // articlesByCategory.put(category.getName(),
    // articleService.getArticlesByCategory(category.getCategoryId()));
    // }

    // model.addAttribute("categories", categories);
    // model.addAttribute("articlesByCategory", articlesByCategory);
    // return "discoverArticles"; // Ensure your HTML file is named
    // articlereview.htm   
    // }
    // @GetMapping("/articles/category/{id}")
    // public String getArticlesByCategory(@PathVariable Long id, Model model) {
    // List<Category> categories = categoryService.getCategory();
    // List<Article> articles = articleService.getArticlesByCategory(id);

    // model.addAttribute("categories", categories);
    // model.addAttribute("articles", articles); // Pass articles of the selected
    // category

    // return "discoverArticles";
    // }

    // @GetMapping("/articles/{id}")
    // public String getArticleById(@PathVariable Long id, Model model) {
    // Article article = articleService.getArticleById(id);
    // List<Category> categories = categoryService.getCategory();

    // model.addAttribute("categories", categories);
    // model.addAttribute("article", article); // Pass selected article

    // return "discoverArticles";
    // }
    @GetMapping("/discoverarticles")
    public String showDiscoverArticles(@RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "articleId", required = false) Long articleId,
            Model model) {
        // Load all categories
        List<Category> categories = categoryService.getCategory();
        model.addAttribute("categories", categories);

        // If a category is selected, fetch its articles
        if (categoryId != null) {
            List<Article> articles = articleService.getArticlesByCategory(categoryId);
            model.addAttribute("articles", articles);
            model.addAttribute("selectedCategoryId", categoryId);
        }

        // If an article is selected, fetch its details
        if (articleId != null) {
            Article article = articleService.getArticleById(articleId);
            model.addAttribute("selectedArticle", article);
        }

        return "discoverArticles"; // Ensure this matches your Thymeleaf template
    }

}
