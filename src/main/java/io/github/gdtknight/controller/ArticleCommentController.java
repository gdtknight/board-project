package io.github.gdtknight.controller;

import io.github.gdtknight.security.BoardPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.gdtknight.dto.UserAccountDto;
import io.github.gdtknight.dto.request.ArticleCommentRequest;
import io.github.gdtknight.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

  private final ArticleCommentService articleCommentService;

  @PostMapping("/new")
  public String postNewArticleComment(
          @AuthenticationPrincipal BoardPrincipal boardPrincipal,
          ArticleCommentRequest articleCommentRequest
  ) {
    articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));

    return "redirect:/articles/" + articleCommentRequest.articleId();
  }

  @PostMapping("/{commentId}/delete")
  public String deleteArticleComment(
          @PathVariable Long commentId,
          @AuthenticationPrincipal BoardPrincipal boardPrincipal,
          Long articleId
  ) {
    articleCommentService.deleteArticleComment(commentId,boardPrincipal.getUsername());

    return "redirect:/articles/" + articleId;
  }

}
