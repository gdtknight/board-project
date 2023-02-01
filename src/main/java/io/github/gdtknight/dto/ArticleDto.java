package io.github.gdtknight.dto;

import java.time.LocalDateTime;

import io.github.gdtknight.domain.Article;

public record ArticleDto(
    Long id,
    UserAccountDto userAccountDto,
    String title,
    String content,
    String hashtag,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy) {

  public static ArticleDto of(
      Long id,
      UserAccountDto userAccountDto,
      String title, String content, String hashtag,
      LocalDateTime createdAt, String createdBy,
      LocalDateTime modifiedAt, String modifiedBy) {
    return new ArticleDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
  }

  public static ArticleDto fromEntity(Article entity) {
    return new ArticleDto(
        entity.getId(),
        UserAccountDto.fromEntity(entity.getUserAccount()),
        entity.getTitle(),
        entity.getContent(),
        entity.getHashtag(),
        entity.getCreatedAt(),
        entity.getCreatedBy(),
        entity.getModifiedAt(),
        entity.getModifiedBy());
  }

  public Article toEntity() {
    return Article.of(
        userAccountDto.toEntity(),
        title,
        content,
        hashtag);
  }
}