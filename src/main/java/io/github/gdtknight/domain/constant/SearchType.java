package io.github.gdtknight.domain.constant;

import lombok.Getter;

public enum SearchType {
  TITLE("제목"),
  CONTENT("본문"),
  ID("유저 ID"),
  NICKNAME("닉네임"),
  HASHTAG("해시태그");

  @Getter
  private final String description;

  private SearchType(String description) {
    this.description = description;
  }

}
