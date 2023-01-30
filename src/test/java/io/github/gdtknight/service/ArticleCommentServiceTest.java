package io.github.gdtknight.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.ArticleComment;
import io.github.gdtknight.dto.ArticleCommentDto;
import io.github.gdtknight.dto.ArticleCommentUpdateDto;
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
    given(articleRepository.findById(articleId)).willReturn(Optional.of(any(Article.class)));

    // when
    List<ArticleCommentDto> articleCommentDtoList = articleCommentService
        .searchArticleCommentByArticleId(articleId);

    // then
    assertThat(articleCommentDtoList).isNotNull();
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("댓글을 조회하면, 댓글을 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
    // given
    Long articleCommentId = 1L;

    // when
    ArticleCommentDto articleCommentDto = articleCommentService
        .searchArticleCommentByArticleCommentId(articleCommentId);

    // then
    assertThat(articleCommentDto).isNotNull();
    then(articleCommentRepository).should().findById(articleCommentId);
  }

  @DisplayName("댓글 정보를 입력하면, 댓글을 생성한다.")
  @Test
  void givenArticleCommentInfo_whenSavingArticleComment_thenSaveArticleComment() {
    // given
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(any(ArticleComment.class));

    // when
    articleCommentService.saveArticleComment(
        ArticleCommentDto.of(LocalDateTime.now(), "Uno", LocalDateTime.now(), "Uno", "content"));

    // then
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }

  @DisplayName("댓글 ID와 수정 정보를 입력하면, 댓글을 수정한다.")
  @Test
  void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    // given
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(any(ArticleComment.class));

    // when
    articleCommentService.updateArticleComment(1L, ArticleCommentUpdateDto.of("content"));

    // then
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }

  @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    // given
    given(articleCommentRepository.findById(anyLong())).willReturn(null);

    // when
    articleCommentService.deleteArticleComment(1L);

    // then
    then(articleCommentRepository).should().deleteById(anyLong());
  }
}
