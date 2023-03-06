package io.github.gdtknight.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {
  List<String> findAllDistinctHashtags();
}
