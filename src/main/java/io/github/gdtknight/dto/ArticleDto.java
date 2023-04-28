package io.github.gdtknight.dto;

import java.time.LocalDateTime;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.UserAccount;

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
      String title,
      String content,
      String hashtag,
      LocalDateTime createdAt,
      String createdBy,
      LocalDateTime modifiedAt,
      String modifiedBy) {
    return new ArticleDto(
        id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
  }

  public static ArticleDto of(
      UserAccountDto userAccountDto, String title, String content, String hashtag) {
    return new ArticleDto(null, userAccountDto, title, content, hashtag, null, null, null, null);
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

  public Article toEntity(UserAccount userAccount) {
    return Article.of(userAccount, title, content, hashtag);
  }
}
