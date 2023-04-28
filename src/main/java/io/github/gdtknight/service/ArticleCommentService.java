package io.github.gdtknight.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.ArticleComment;
import io.github.gdtknight.domain.UserAccount;
import io.github.gdtknight.dto.ArticleCommentDto;
import io.github.gdtknight.repository.ArticleCommentRepository;
import io.github.gdtknight.repository.ArticleRepository;
import io.github.gdtknight.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;
  private final UserAccountRepository userAccountRepository;

  @Transactional(readOnly = true)
  public List<ArticleCommentDto> searchArticleCommentByArticleId(Long articleId) {
    return articleCommentRepository.findByArticle_Id(articleId).stream()
        .map(ArticleCommentDto::fromEntity)
        .toList();
  }

  public ArticleCommentDto searchArticleCommentByArticleCommentId(Long articleCommentId) {
    return null;
  }

  public void saveArticleComment(ArticleCommentDto dto) {
    try {
      Article article = articleRepository.getReferenceById(dto.articleId());
      UserAccount userAccount =
          userAccountRepository.getReferenceById(dto.userAccountDto().userId());
      articleCommentRepository.save(dto.toEntity(article, userAccount));
    } catch (EntityNotFoundException e) {
      log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
    }
  }

  public void updateArticleComment(ArticleCommentDto dto) {
    try {
      ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
      if (dto.content() != null) {
        articleComment.setContent(dto.content());
      }
    } catch (EntityNotFoundException e) {
      log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto: {}", dto);
    }
  }

  public void deleteArticleComment(long articleCommentId, String userId) {
    articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
  }
}
