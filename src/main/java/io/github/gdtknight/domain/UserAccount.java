package io.github.gdtknight.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(
    indexes = {
      @Index(columnList = "email", unique = true),
      @Index(columnList = "createdAt"),
      @Index(columnList = "createdBy")
    })
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserAccount extends AuditingFields {
  @Id
  @Column(length = 50)
  private String userId;

  @Setter
  @Column(nullable = false)
  private String userPassword;

  @Setter
  @Column(length = 100)
  private String email;

  @Setter
  @Column(length = 100)
  private String nickname;

  @Setter private String memo;

  private UserAccount(
      String userId, String userPassword, String email, String nickname, String memo) {
    this.userId = userId;
    this.userPassword = userPassword;
    this.email = email;
    this.nickname = nickname;
    this.memo = memo;
  }

  public static UserAccount of(
      String userId, String userPassword, String email, String nickname, String memo) {
    return new UserAccount(userId, userPassword, email, nickname, memo);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserAccount userAccount)) return false;
    return userId != null && userId.equals(userAccount.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }
}
