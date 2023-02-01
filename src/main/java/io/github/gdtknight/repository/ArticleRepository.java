package io.github.gdtknight.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;

import io.github.gdtknight.domain.Article;
import io.github.gdtknight.domain.QArticle;

@RepositoryRestResource
public interface ArticleRepository extends
    JpaRepository<Article, Long>,
    QuerydslPredicateExecutor<Article>, // 모든 필드에 대한 기본 검색 기능 구현
    QuerydslBinderCustomizer<QArticle> {

  @Override
  default void customize(QuerydslBindings bindings, QArticle root) {

    bindings.excludeUnlistedProperties(true); // 특정 필드에 관하서만 검색이 가능하도록
    bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
    // bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like
    // '${value}' 부분 검색 수동 설정할 때
    bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%S{value}%'
    bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%S{value}%'
    bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); // like '%S{value}%'
    bindings.bind(root.createdAt).first(DateTimeExpression::eq); // like '%S{value}%'
    bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // like '%S{value}%'

  }

  Page<Article> findByTitle(String searchKeyword, Pageable pageable);
}
