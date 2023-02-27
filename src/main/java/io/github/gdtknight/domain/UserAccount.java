package io.github.gdtknight.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(indexes = {
    @Index(columnList = "userId", unique = true),
    @Index(columnList = "email", unique = true),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserAccount extends AuditingFields {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false, length = 50)
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
  @Setter
  private String memo;

  private UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
    this.userId = userId;
    this.userPassword = userPassword;
    this.email = email;
    this.nickname = nickname;
    this.memo = memo;
  }

  public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
    return new UserAccount(
        userId, userPassword, email, nickname, memo);
  }
}
