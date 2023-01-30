package io.github.gdtknight.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.type.SearchType;
import io.github.gdtknight.dto.ArticleDto;
import io.github.gdtknight.dto.ArticleUpdateDto;
import io.github.gdtknight.repository.ArticleRepository;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
  @InjectMocks
  private ArticleService articleService;

  @Mock
  private ArticleRepository articleRepository;

  @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
  @Test
  void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList() {
    // given

    // when
    Page<ArticleDto> articleDtoList = articleService.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, ID,
                                                                                                         // 닉네임, 해시태그

    // then
    assertThat(articleDtoList).isNotNull();
  }

  @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
    // given

    // when
    ArticleDto articleDto = articleService.searchArticle(1L); // 제목, 본문, ID,
                                                              // 닉네임, 해시태그

    // then
    assertThat(articleDto).isNotNull();
  }

  @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
  @Test
  void givenArticleInfo_whenSavingArticle_thenSaveArticle() {
    // given
    given(articleRepository.save(any(Article.class))).willReturn(any(Article.class));

    // when
    articleService.saveArticle(ArticleDto.of(LocalDateTime.now(), "Uno", "title", "content", "#java"));

    // then
    then(articleRepository).should().save(any(Article.class));
  }

  @DisplayName("게시글 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
  @Test
  void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    // given
    given(articleRepository.save(any(Article.class))).willReturn(any(Article.class));

    // when
    articleService.updateArticle(1L, ArticleUpdateDto.of("title", "content", "#java"));

    // then
    then(articleRepository).should().save(any(Article.class));
  }

  @DisplayName("게시글 ID를 입력하면, 게시글을 삭제한다.")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    // given
    given(articleRepository.findById(anyLong())).willReturn(null);

    // when
    articleService.deleteArticle(1L);

    // then
    then(articleRepository).should().deleteById(anyLong());
  }
}
