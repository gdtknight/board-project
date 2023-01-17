package io.github.gdtknight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gdtknight.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
