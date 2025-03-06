package com.rahul.controller;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rahul.model.Article;
import com.rahul.model.Comment;
import com.rahul.model.Users;
import com.rahul.repository.ArticleRepository;
import com.rahul.repository.CommentRepository;
import com.rahul.repository.UserRepository;


@Controller
public class CommentController {
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ArticleRepository articlerepo;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private CommentRepository commentrepo;


@PostMapping("/comments")
@ResponseBody
public ResponseEntity<String> postMethodName(@RequestParam Long articleId, @RequestParam String username, @RequestParam String content) {
    Article article = articlerepo.findById(articleId)
                                           .orElseThrow(() -> new RuntimeException("Article not found"));
    
    Users user = userrepo.findByfirstName(username)
                                   .orElseThrow(() -> new RuntimeException("User not found"));

    Comment comment = new Comment();
    if(article==null){
        logger.error("ERROR:Article not found: ");
    }
   
    comment.setArticle(article);
    comment.setContent(content);
    comment.setUser(user);;
    commentrepo.save(comment);

     return ResponseEntity.status(HttpStatus.CREATED).body("Comment added successfully!");
    }


}
