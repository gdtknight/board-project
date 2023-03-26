package io.github.gdtknight.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.UserAccount;
import io.github.gdtknight.domain.constant.SearchType;
import io.github.gdtknight.dto.ArticleDto;
import io.github.gdtknight.dto.ArticleWithCommentsDto;
import io.github.gdtknight.repository.ArticleRepository;
import io.github.gdtknight.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

  private final UserAccountRepository userAccountRepository;
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
  public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
    return articleRepository.findById(articleId)
        .map(ArticleWithCommentsDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId)); // 운영 편의
  }

  @Transactional(readOnly = true)
  public ArticleDto getArticle(Long articleId) {
    return articleRepository.findById(articleId)
        .map(ArticleDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
  }

  public void saveArticle(ArticleDto dto) {
    UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
    articleRepository.save(dto.toEntity(userAccount));
  }

  public void updateArticle(Long articleId, ArticleDto dto) {
    try {
      Article article = articleRepository.getReferenceById(articleId);
      UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

      if (article.getUserAccount().equals(userAccount)) {
        if (dto.title()   != null) { article.setTitle(dto.title()); }
        if (dto.content() != null) { article.setContent(dto.content()); }
        article.setHashtag(dto.hashtag());
      }

      articleRepository.save(article);
    } catch (EntityNotFoundException e) {
      log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage()); // String interpolation
    }
  }

  public void deleteArticle(long articleId, String userId) {
    articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
  }

  public long getArticleCount() {
    return articleRepository.count();
  }

  @Transactional(readOnly = true)
  public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
    if (hashtag == null || hashtag.isBlank()) {
      return Page.empty(pageable);
    }

    return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::fromEntity);
  }

  public List<String> getHashtags() {
    return articleRepository.findAllDistinctHashtags();
  }

}
