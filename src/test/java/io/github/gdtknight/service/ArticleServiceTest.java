package io.github.gdtknight.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.UserAccount;
import io.github.gdtknight.domain.type.SearchType;
import io.github.gdtknight.dto.ArticleDto;
import io.github.gdtknight.dto.ArticleWithCommentsDto;
import io.github.gdtknight.dto.UserAccountDto;
import io.github.gdtknight.repository.ArticleRepository;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
  @InjectMocks
  private ArticleService articleService;

  @Mock
  private ArticleRepository articleRepository;

  @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
  @Test
  void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
    // given
    Pageable pageable = Pageable.ofSize(20);
    given(articleRepository.findAll(pageable)).willReturn(Page.empty());

    // when
    Page<ArticleDto> articles = articleService.searchArticles(null, null, pageable);

    // then
    assertThat(articles).isEmpty();
    then(articleRepository).should().findAll(pageable);
  }

  @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
  @Test
  void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
    // given
    SearchType searchType = SearchType.TITLE;
    String searchKeyword = "title";
    Pageable pageable = Pageable.ofSize(20);
    given(articleRepository.findByTitle(searchKeyword, pageable)).willReturn(Page.empty());

    // when
    Page<ArticleDto> articles = articleService.searchArticles(searchType, searchKeyword, pageable);

    // then
    assertThat(articles).isEmpty();
    then(articleRepository).should().findAll(pageable);
  }

  @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
    // given
    Long articleId = 1L;
    Article article = createArticle();
    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

    // when
    ArticleWithCommentsDto dto = articleService.getArticle(articleId);

    // then
    assertThat(dto)
        .hasFieldOrPropertyWithValue("title", article.getTitle())
        .hasFieldOrPropertyWithValue("content", article.getContent())
        .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
  @Test
  void givenNoneexistentArticleId_whenSearchingArticle_thenThrowsException() {
    // given
    Long articleId = 0L;
    given(articleRepository.findById(articleId)).willReturn(Optional.empty());

    // when
    Throwable t = catchThrowable(() -> articleService.getArticle(articleId));

    // then
    assertThat(t)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("게시글이 없습니다 - articleId: " + articleId);
    then(articleRepository).should().findById(articleId);

  }

  @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
  @Test
  void givenArticleInfo_whenSavingArticle_thenSaveArticle() {
    // given
    ArticleDto dto = createArticleDto();
    given(articleRepository.save(any(Article.class))).willReturn(createArticle());

    // when
    articleService.saveArticle(dto);

    // then
    then(articleRepository).should().save(any(Article.class));
  }

  @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
  @Test
  void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    // given
    Article article = createArticle();
    ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
    given(articleRepository.getReferenceById(dto.id())).willReturn(article);

    // when
    articleService.updateArticle(dto);

    // then
    assertThat(article)
        .hasFieldOrPropertyWithValue("title", dto.title())
        .hasFieldOrPropertyWithValue("content", dto.content())
        .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
    then(articleRepository).should().save(any(Article.class));
  }

  @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무것도 하지 않는다.")
  @Test
  void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
    // given
    ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
    given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

    // when
    articleService.updateArticle(dto);

    // then
    then(articleRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("게시글 ID를 입력하면, 게시글을 삭제한다.")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    // given
    Long articleId = 1L;
    willDoNothing().given(articleRepository).deleteById(articleId);

    // when
    articleService.deleteArticle(1L);

    // then
    then(articleRepository).should().deleteById(articleId);
  }

  private UserAccount createUserAccount() {
    return UserAccount.of(
        "uno",
        "password",
        "uno@email.com",
        "Uno",
        null);
  }

  private Article createArticle() {
    return Article.of(
        createUserAccount(),
        "title",
        "content",
        "#java");
  }

  private ArticleDto createArticleDto() {
    return createArticleDto("title", "content", "#java");
  }

  private ArticleDto createArticleDto(String title, String content, String hashtag) {
    return ArticleDto.of(1L,
        createUserAccountDto(),
        title,
        content,
        hashtag,
        LocalDateTime.now(),
        "Uno",
        LocalDateTime.now(),
        "Uno");
  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
        1L,
        "uno",
        "password",
        "uno@mail.com",
        "Uno",
        "This is memo",
        LocalDateTime.now(),
        "uno",
        LocalDateTime.now(),
        "uno");
  }
}
