package io.github.gdtknight.dto;

import java.time.LocalDateTime;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.ArticleComment;
import io.github.gdtknight.domain.UserAccount;

public record ArticleCommentDto(
    Long id,
    Long articleId,
    UserAccountDto userAccountDto,
    String content,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy) {

  public static ArticleCommentDto of(
      Long articleId, UserAccountDto userAccountDto, String content) {
    return new ArticleCommentDto(null, articleId, userAccountDto, content, null, null, null, null);
  }

  public static ArticleCommentDto of(
      Long id,
      Long articleId,
      UserAccountDto userAccountDto,
      String content,
      LocalDateTime createdAt,
      String createdBy,
      LocalDateTime modifiedAt,
      String modifiedBy) {
    return new ArticleCommentDto(
        id, articleId, userAccountDto, content, createdAt, createdBy, modifiedAt, modifiedBy);
  }

  public static ArticleCommentDto fromEntity(ArticleComment entity) {
    return new ArticleCommentDto(
        entity.getId(),
        entity.getArticle().getId(),
        UserAccountDto.fromEntity(entity.getUserAccount()),
        entity.getContent(),
        entity.getCreatedAt(),
        entity.getCreatedBy(),
        entity.getModifiedAt(),
        entity.getModifiedBy());
  }

  public ArticleComment toEntity(Article article, UserAccount userAccount) {
    return ArticleComment.of(article, userAccount, content);
  }
}
