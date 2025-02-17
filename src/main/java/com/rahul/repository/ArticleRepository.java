package com.rahul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Article;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCategoryCategoryId(Long categoryId);
}