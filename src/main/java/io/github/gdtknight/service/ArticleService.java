package io.github.gdtknight.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.type.SearchType;
import io.github.gdtknight.dto.ArticleDto;
import io.github.gdtknight.dto.ArticleUpdateDto;
import io.github.gdtknight.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

  private final ArticleRepository articleRepository;

  @Transactional(readOnly = true)
  public Page<ArticleDto> searchArticles(SearchType title, String string) {
    return Page.empty();
  }

  public ArticleDto searchArticle(long articleId) {
    Optional<Article> article = articleRepository.findById(articleId);
    return null;
  }

  public void saveArticle(ArticleDto articleDto) {
    articleRepository.save(Article.of(articleDto.title(), articleDto.content(), articleDto.hashtag()));
  }

  public void updateArticle(long articleId, ArticleUpdateDto of) {
  }

  public void deleteArticle(long l) {
  }

}
