package io.github.gdtknight.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import java.time.LocalDateTime;
import java.util.List;
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
import io.github.gdtknight.domain.constant.SearchType;
import io.github.gdtknight.dto.ArticleDto;
import io.github.gdtknight.dto.ArticleWithCommentsDto;
import io.github.gdtknight.dto.UserAccountDto;
import io.github.gdtknight.repository.ArticleRepository;
import io.github.gdtknight.repository.UserAccountRepository;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
  @InjectMocks
  private ArticleService articleService;

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private UserAccountRepository userAccountRepository;

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
    given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

    // when
    Page<ArticleDto> articles = articleService.searchArticles(searchType, searchKeyword, pageable);

    // then
    assertThat(articles).isEmpty();
    then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
  }

  @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
  @Test
  void givenNoSearchParameters_whenSearchingArticlesViaHashTag_thenReturnsEmptyPage() {
    // given
    Pageable pageable = Pageable.ofSize(20);

    // when
    Page<ArticleDto> articles = articleService.searchArticlesViaHashtag(null, pageable);

    // then
    assertThat(articles).isEqualTo(Page.empty(pageable));
    then(articleRepository).shouldHaveNoInteractions();
  }

  @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
  @Test
  void givenHashtag_whenSearchingArticlesViaHashTag_thenReturnsArticlesPage() {
    // given
    String hashtag = "#java";
    Pageable pageable = Pageable.ofSize(20);
    given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));

    // when
    Page<ArticleDto> articles = articleService.searchArticlesViaHashtag(hashtag, pageable);

    // then
    assertThat(articles).isEqualTo(Page.empty(pageable));
    then(articleRepository).should().findByHashtag(hashtag, pageable);
  }

  @DisplayName("게시글 ID로 조회하면, 댓글 달긴 게시글을 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticleWithComments_thenReturnsArticleWithComments() {
    // Given
    Long articleId = 1L;
    Article article = createArticle();
    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

    // When
    ArticleWithCommentsDto dto = articleService.getArticleWithComments(articleId);

    // Then
    assertThat(dto)
        .hasFieldOrPropertyWithValue("title", article.getTitle())
        .hasFieldOrPropertyWithValue("content", article.getContent())
        .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("댓글 달린 게시글이 없으면, 예외를 던진다.")
  @Test
  void givenNonexistentArticleId_whenSearchingArticleWithComments_thenThrowsException() {
    // Given
    Long articleId = 0L;
    given(articleRepository.findById(articleId)).willReturn(Optional.empty());

    // When
    Throwable t = catchThrowable(() -> articleService.getArticleWithComments(articleId));

    // Then
    assertThat(t)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("게시글이 없습니다 - articleId: " + articleId);
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
    // given
    Long articleId = 1L;
    Article article = createArticle();
    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

    // when
    ArticleDto dto = articleService.getArticle(articleId);

    // then
    assertThat(dto)
        .hasFieldOrPropertyWithValue("title", article.getTitle())
        .hasFieldOrPropertyWithValue("content", article.getContent())
        .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("게시글이 없으면, 예외를 던진다.")
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
    given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
    given(articleRepository.save(any(Article.class))).willReturn(createArticle());

    // when
    articleService.saveArticle(dto);

    // then
    then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
    then(articleRepository).should().save(any(Article.class));
  }

  @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
  @Test
  void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    // given
    Article article = createArticle();
    ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
    given(articleRepository.getReferenceById(dto.id())).willReturn(article);
    given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(dto.userAccountDto().toEntity());

    // when
    articleService.updateArticle(dto.id(), dto);

    // then
    assertThat(article)
        .hasFieldOrPropertyWithValue("title", dto.title())
        .hasFieldOrPropertyWithValue("content", dto.content())
        .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
    then(articleRepository).should().save(any(Article.class));
    then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
  }

  @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무것도 하지 않는다.")
  @Test
  void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
    // given
    ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
    given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

    // when
    articleService.updateArticle(dto.id(), dto);

    // then
    then(articleRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("게시글 ID를 입력하면, 게시글을 삭제한다.")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    // Given
    Long articleId = 1L;
    String userId = "uno";
    willDoNothing().given(articleRepository).deleteByIdAndUserAccount_UserId(articleId, userId);

    // When
    articleService.deleteArticle(1L, userId);

    // Then
    then(articleRepository).should().deleteByIdAndUserAccount_UserId(articleId, userId);
  }

  @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다")
  @Test
  void givenNothing_whenCountingArticles_thenReturnsArticleCount() {
    // Given
    long expected = 0L;
    given(articleRepository.count()).willReturn(expected);
    // When
    long actual = articleService.getArticleCount();
    // Then
    assertThat(actual).isEqualTo(expected);
    then(articleRepository).should().count();
  }

  @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다.")
  @Test
  void givenNothing_whenCalling_thenReturnsHashtags() {
    // Given
    List<String> expectedHashtags = List.of("#java", "#spring", "#boot");
    given(articleRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);

    // When
    List<String> actualHashtags = articleService.getHashtags();

    // Then
    assertThat(actualHashtags).isEqualTo(expectedHashtags);
    then(articleRepository).should().findAllDistinctHashtags();
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
