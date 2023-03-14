package io.github.gdtknight.controller;

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
  public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {
    // TODO: 인증 정보를 넣어줘야 한다.
    articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
        "uno", "pw", "uno@mail.com", null, null)));

    return "redirect:/articles/" + articleCommentRequest.articleId();
  }

  @PostMapping("/{commentId}/delete")
  public String deleteArticleComment(@PathVariable Long commentId, Long articleId) {
    articleCommentService.deleteArticleComment(commentId);

    return "redirect:/articles/" + articleId;
  }

}
