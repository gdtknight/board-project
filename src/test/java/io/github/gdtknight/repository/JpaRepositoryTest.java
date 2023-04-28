package io.github.gdtknight.repository;

import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import io.github.gdtknight.config.JpaConfig;
import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.UserAccount;

/**
 * @ActiveProfiles @AutoConfigureTestDatabase 어노테이션을 통해
 * @DataJpaTest 어노테이션이 activeprofile 을 무시하는 현상 방지
 */
@ActiveProfiles("testdb")
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
public class JpaRepositoryTest {
  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;
  private final UserAccountRepository userAccountRepository;

  public JpaRepositoryTest(
      @Autowired ArticleRepository articleRepository,
      @Autowired ArticleCommentRepository articleCommentRepository,
      @Autowired UserAccountRepository userAccountRepository) {
    this.articleRepository = articleRepository;
    this.articleCommentRepository = articleCommentRepository;
    this.userAccountRepository = userAccountRepository;
  }

  @DisplayName("select 테스트")
  @Test
  void givenTestData_whenSelecting_thenWorksFine() {
    // given

    // when
    List<Article> articles = articleRepository.findAll();

    // then
    assertThat(articles)
        .isNotNull()
        .hasSize(123);
  }

  @DisplayName("insert 테스트")
  @Test
  void givenTestData_whenInserting_thenWorksFine() {
    // given
    long previousCount = articleRepository.count();
    UserAccount userAccount = userAccountRepository.save(UserAccount.of("uno", "pw", null, null, null));
    Article article = Article.of(userAccount, "new article", "new content", "#spring");

    // when
    articleRepository.save(article);

    // then
    assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
  }

  @DisplayName("update 테스트")
  @Test
  void givenTestData_whenUpdating_thenWorksFine() {
    // given
    Article article = articleRepository.findById(1L).orElseThrow();
    String updatedHashTag = "#springboot";
    article.setHashtag(updatedHashTag);

    // when
    Article savedArticle = articleRepository.saveAndFlush(article);

    // then
    assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashTag);
  }

  @DisplayName("delete 테스트")
  @Test
  void givenTestData_whenDeleting_thenWorksFine() {
    // given
    Article article = articleRepository.findById(1L).orElseThrow();
    long previousArticleCount = articleRepository.count();
    long previousArticleCommentCount = articleCommentRepository.count();
    long deletedCommentsSize = article.getArticleComments().size();

    // when
    articleRepository.delete(article);

    // then
    assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
    assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
  }

  @EnableJpaAuditing
  @TestConfiguration
  public static class TestJpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
      return () -> Optional.of("uno");
    }
  }
}
