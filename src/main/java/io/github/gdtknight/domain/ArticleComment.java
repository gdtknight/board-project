package io.github.gdtknight.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
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
    @Index(columnList = "content"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false) // cascade 는 기본적으로 none
  @Setter
  private Article article; // 게시글 (ID)

  @Setter
  @Column(nullable = false, length = 500)
  private String content; // 본문

  // meta-information
  @CreatedDate
  private LocalDateTime createdAt; // 생성일시
  @CreatedBy
  @Column(nullable = false, length = 100)
  private String createdBy; // 생성자
  @LastModifiedDate
  private LocalDateTime modifiedAt; // 수정일시
  @LastModifiedBy
  @Column(nullable = false, length = 100)
  private String modifiedBy; // 수정자

  private ArticleComment(Article article, String content) {
    this.article = article;
    this.content = content;
  }

  public static ArticleComment of(Article article, String content) {
    return new ArticleComment(article, content);
  }

  // Equals, HashCode -> Lombok을 활용해서 생성하는 경우
  // Entity 내의 모든 필드를 비교하는 비효율적인 방법으로 검사
  // id가 PK이므로 이를 활용해서 Entity의 성격을 반영함과 동시에
  // 코드 효을성을 보장한다

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  // Entity를 Database 에 영속화시키고 연결짓고 사용하는 환경에서
  // 서로 다른 두 row (entity)가 구별되기 위한 조건
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof ArticleComment articleComment))
      return false;
    // id가 부여되지 않았다면, 동등성 검사 자체가 의미없음
    return id != null && id.equals(articleComment.id);
  }
}
