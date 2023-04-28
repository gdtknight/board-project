package io.github.gdtknight.dto.request;

import io.github.gdtknight.dto.ArticleCommentDto;
import io.github.gdtknight.dto.UserAccountDto;

public record ArticleCommentRequest(Long articleId, String content) {

  public static ArticleCommentRequest of(Long articleId, String content) {
    return new ArticleCommentRequest(articleId, content);
  }

  public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
    return ArticleCommentDto.of(articleId, userAccountDto, content);
  }
}
