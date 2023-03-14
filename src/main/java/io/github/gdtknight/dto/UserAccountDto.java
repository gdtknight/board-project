package io.github.gdtknight.dto;

import java.time.LocalDateTime;

import io.github.gdtknight.domain.UserAccount;

public record UserAccountDto(
    String userId,
    String userPassword,
    String email,
    String nickname,
    String memo,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy) {

  public static UserAccountDto of(
      String userId, String userPassword,
      String email, String nickname, String memo,
      LocalDateTime createdAt, String createdBy,
      LocalDateTime modifiedAt, String modifiedBy) {

    return new UserAccountDto(
        userId, userPassword,
        email, nickname, memo,
        createdAt, createdBy,
        modifiedAt, modifiedBy);
  }

  public static UserAccountDto fromEntity(UserAccount entity) {
    return new UserAccountDto(
        entity.getUserId(),
        entity.getUserPassword(),
        entity.getEmail(),
        entity.getNickname(),
        entity.getMemo(),
        entity.getCreatedAt(),
        entity.getCreatedBy(),
        entity.getModifiedAt(),
        entity.getModifiedBy());
  }

  public UserAccount toEntity() {

    return UserAccount.of(
        userId,
        userPassword,
        email,
        nickname,
        memo);
  }

}
