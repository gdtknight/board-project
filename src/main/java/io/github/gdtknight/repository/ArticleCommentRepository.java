package io.github.gdtknight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gdtknight.domain.ArticleComment;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

}
