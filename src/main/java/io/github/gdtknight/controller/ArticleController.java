package io.github.gdtknight.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.gdtknight.domain.type.SearchType;
import io.github.gdtknight.dto.ArticleResponse;
import io.github.gdtknight.dto.ArticleWithCommentsResponse;
import io.github.gdtknight.service.ArticleService;
import io.github.gdtknight.service.PaginationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

  private final ArticleService articleService;
  private final PaginationService paginationService;

  @GetMapping
  public String articles(
      @RequestParam(required = false) SearchType searchType,
      @RequestParam(required = false) String searchValue,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
      ModelMap map) {
    Page<ArticleResponse> articles = articleService
        .searchArticles(searchType, searchValue, pageable)
        .map(ArticleResponse::fromArticleDto);

    List<Integer> barNumbers = paginationService
        .getPaginationBarNumbers(
            pageable.getPageNumber(),
            articles.getTotalPages());
    map.addAttribute("articles", articles);
    map.addAttribute("paginationBarNumbers", barNumbers);
    map.addAttribute("searchTypes", searchType.values());

    return "articles/index";

  }

  @GetMapping("/{articleId}")
  public String article(@PathVariable Long articleId, ModelMap map) {
    ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
    map.addAttribute("article", article);
    map.addAttribute("articleComments", article.articleCommentsResponse());
    return "articles/detail";
  }
}
