package io.github.gdtknight.domain;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)
@Table(indexes = {
    @Index(columnList = "title"),
    @Index(columnList = "hashtag"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article extends AuditingFields {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @ManyToOne(optional = false)
  private UserAccount userAccount;

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

  @ToString.Exclude // 순환 참조 문제 발생 가능성 제거
  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

  private Article(UserAccount userAccount, String title, String content, String hashtag) {
    this.userAccount = userAccount;
    this.title = title;
    this.content = content;
    this.hashtag = hashtag;
  }

  // 생성자들은 protected, private 으로 접근을 방지
  // static method 를 활용하여 생성된 객체를 반환
  public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
    return new Article(userAccount, title, content, hashtag);
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
