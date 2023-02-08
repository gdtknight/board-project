package io.github.gdtknight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;

import io.github.gdtknight.domain.ArticleComment;
import io.github.gdtknight.domain.QArticleComment;

@RepositoryRestResource
public interface ArticleCommentRepository extends
    JpaRepository<ArticleComment, Long>,
    QuerydslPredicateExecutor<ArticleComment>,
    QuerydslBinderCustomizer<QArticleComment>
//
{

  @Override
  default void customize(QuerydslBindings bindings, QArticleComment root) {
    // TODO Auto-generated method stub
    bindings.excludeUnlistedProperties(true); // 특정 필드에 관하서만 검색이 가능하도록
    bindings.including(root.content, root.createdAt, root.createdBy);
    // bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like
    // '${value}' 부분 검색 수동 설정할 때
    bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%S{value}%'
    bindings.bind(root.createdAt).first(DateTimeExpression::eq); // like '%S{value}%'
    bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // like '%S{value}%'
  }

  List<ArticleComment> findByArticle_Id(Long articleId);

}
