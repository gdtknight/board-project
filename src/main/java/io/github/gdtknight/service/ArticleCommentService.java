package io.github.gdtknight.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.gdtknight.dto.ArticleCommentDto;
import io.github.gdtknight.dto.ArticleCommentUpdateDto;
import io.github.gdtknight.repository.ArticleCommentRepository;
import io.github.gdtknight.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;

  @Transactional(readOnly = true)
  public List<ArticleCommentDto> searchArticleCommentByArticleId(Long articleId) {
    return List.of();
  }

  public ArticleCommentDto searchArticleCommentByArticleCommentId(Long articleCommentId) {
    return null;
  }

  public void saveArticleComment(ArticleCommentDto of) {
  }

  public void updateArticleComment(long articleCommentId, ArticleCommentUpdateDto of) {
  }

  public void deleteArticleComment(long l) {
  }
}
