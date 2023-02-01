package io.github.gdtknight.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.type.SearchType;
import io.github.gdtknight.dto.ArticleDto;
import io.github.gdtknight.dto.ArticleWithCommentsDto;
import io.github.gdtknight.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    return switch (searchType) {
      case TITLE ->
        articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::fromEntity);

      case CONTENT ->
        articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::fromEntity);

      case ID ->
        articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::fromEntity);

      case NICKNAME ->
        articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::fromEntity);

      case HASHTAG ->
        articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::fromEntity);
    };

  }

  @Transactional(readOnly = true)
  public ArticleWithCommentsDto getArticle(Long articleId) {
    return articleRepository.findById(articleId)
        .map(ArticleWithCommentsDto::from)
        .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
  }

  public void saveArticle(ArticleDto dto) {
    articleRepository.save(dto.toEntity());
  }

  public void updateArticle(ArticleDto dto) {

    try {
      Article article = articleRepository.getReferenceById(dto.id());

      if (dto.title() != null) {
        article.setTitle(dto.title());
      }

      if (dto.content() != null) {
        article.setContent(dto.content());
      }

      article.setHashtag(dto.hashtag());

      articleRepository.save(article);
    } catch (EntityNotFoundException e) {
      log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);

    }

  }

  public void deleteArticle(long articleId) {
    articleRepository.deleteById(articleId);
  }

}
