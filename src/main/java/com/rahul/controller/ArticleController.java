package com.rahul.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rahul.model.Category;
import com.rahul.service.CategoryService;

@Controller
public class ArticleController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/articlewriting")
    public String article(Model model) {
        List<Category> categories = categoryService.getCategory();
        model.addAttribute("categories", categories);
        return "articlewriting";
    }

    @GetMapping("/preview")
    public String articlePreview() {
        return "preview";
    }

    @GetMapping("/articlereview")
    public String articleReview() {
        return "articleReview";
    }

    @GetMapping("/articlereview/preview")
    public String articleAdminPreview() {
        return "adminPreview";
    }
}
