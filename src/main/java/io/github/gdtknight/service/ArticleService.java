package io.github.gdtknight.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.gdtknight.domain.type.SearchType;
import io.github.gdtknight.dto.ArticleDto;
import io.github.gdtknight.dto.ArticleUpdateDto;
import io.github.gdtknight.dto.ArticleWithCommentsDto;
import io.github.gdtknight.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

  private final ArticleRepository articleRepository;

  @Transactional(readOnly = true)
  public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
    if (searchKeyword == null || searchKeyword.isBlank()) {
      return articleRepository.findAll(pageable).map(ArticleDto::fromEntity);
    }
    return Page.empty();
  }

  @Transactional(readOnly = true)
  public ArticleWithCommentsDto getArticle(Long articleId) {
    return null;
  }

  public void saveArticle(ArticleDto dto) {
  }

  public void updateArticle(long articleId, ArticleUpdateDto of) {
  }

  public void deleteArticle(long l) {
  }

  public void updateArticle(ArticleDto dto) {
  }

}
