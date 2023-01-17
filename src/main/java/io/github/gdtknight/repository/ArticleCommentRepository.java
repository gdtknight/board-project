package io.github.gdtknight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.gdtknight.domain.ArticleComment;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

}
