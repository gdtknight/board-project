package io.github.gdtknight.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(indexes = {
    @Index(columnList = "title"),
    @Index(columnList = "hashtag"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@Entity
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // @Setter 를 Class 전체가 아닌 각 필드에 걸어준다
  // 외부 임의 접근 방지 의도

  @Setter
  @Column(nullable = false)
  private String title; // 제목
  @Setter
  @Column(nullable = false, length = 10000)
  private String content; // 본문

  @Setter
  private String hashtag; // 해시태그

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

  private Article(String title, String content, String hashtag) {
    this.title = title;
    this.content = content;
    this.hashtag = hashtag;
  }

  // 생성자들은 protected, private 으로 접근을 방지
  // static method 를 활용하여 생성된 객체를 반환
  public static Article of(String title, String content, String hashtag) {
    return new Article(title, content, hashtag);
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
    if (!(obj instanceof Article article))
      return false;
    // id가 부여되지 않았다면, 동등성 검사 자체가 의미없음
    return id != null && id.equals(article.id);
  }
}
