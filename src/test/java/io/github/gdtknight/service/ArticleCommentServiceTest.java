package io.github.gdtknight.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.ArticleComment;
import io.github.gdtknight.domain.UserAccount;
import io.github.gdtknight.dto.ArticleCommentDto;
import io.github.gdtknight.dto.ArticleCommentUpdateDto;
import io.github.gdtknight.dto.UserAccountDto;
import io.github.gdtknight.repository.ArticleCommentRepository;
import io.github.gdtknight.repository.ArticleRepository;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
public class ArticleCommentServiceTest {
  @InjectMocks
  private ArticleCommentService articleCommentService;

  @Mock
  private ArticleCommentRepository articleCommentRepository;

  @Mock
  private ArticleRepository articleRepository;

  @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
  @Test
  void givenArticleId_whenSearchingComments_thenReturnsArticleComments() {
    // given
    Long articleId = 1L;
    ArticleComment expected = createArticleComment("content");
    given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

    // when
    List<ArticleCommentDto> actual = articleCommentService.searchArticleCommentByArticleId(articleId);

    // then
    assertThat(actual).hasSize(1).first().hasFieldOrPropertyWithValue("content", expected.getContent());
    then(articleCommentRepository).should().findByArticle_Id(articleId);
  }

  @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
  @Test
  void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
    // given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

    // when
    articleCommentService.saveArticleComment(dto);

    // then
    then(articleRepository).should().getReferenceById(dto.articleId());
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }

  @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
  @Test
  void givenNoneexistentArticle_whenSavingArticleComment_thenLogsSituationAndDeosNothing() {
    // given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);

    // when
    articleCommentService.saveArticleComment(dto);

    // then
    then(articleRepository).should().getReferenceById(dto.articleId());
    then(articleCommentRepository).shouldHaveNoInteractions();
  }

  @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
  @Test
  void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    // given
    String oldContent = "content";
    String updatedContent = "댓글";
    ArticleComment articleComment = createArticleComment(oldContent);
    ArticleCommentDto dto = createArticleCommentDto(updatedContent);
    given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);

    // when
    articleCommentService.updateArticleComment(dto);

    // then
    assertThat(articleComment.getContent())
        .isNotEqualTo(oldContent)
        .isEqualTo(updatedContent);

    then(articleCommentRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무것도 안한다.")
  @Test
  void givenNoneexistentArticle_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing() {
    // given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

    // when
    articleCommentService.updateArticleComment(dto);

    // then
    then(articleCommentRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    // given
    Long articleCommentId = 1L;
    willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);

    // when
    articleCommentService.deleteArticleComment(articleCommentId);

    // then
    then(articleCommentRepository).should().deleteById(articleCommentId);
  }

  private ArticleCommentDto createArticleCommentDto(String content) {
    return ArticleCommentDto.of(
        1L,
        1L,
        createUserAccountDto(),
        content,
        LocalDateTime.now(),
        "uno",
        LocalDateTime.now(),
        "uno");
  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
        "uno",
        "password",
        "uno@gmail.com",
        "Uno",
        "This is memo",
        LocalDateTime.now(),
        "uno",
        LocalDateTime.now(),
        "uno");
  }

  private ArticleComment createArticleComment(String content) {
    return ArticleComment.of(
        Article.of(createUserAccount(), "title", "cnotent", "hashtag"),
        createUserAccount(),
        content);
  }

  private UserAccount createUserAccount() {
    return UserAccount.of(
        "uno",
        "password",
        "uno@gmail.com",
        "Uno",
        null);
  }

  private Article createArticle() {
    return Article.of(createUserAccount(),
        "title",
        "content",
        "#java");
  }
}
