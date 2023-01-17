package io.github.gdtknight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.gdtknight.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
