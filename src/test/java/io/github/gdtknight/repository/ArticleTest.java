package io.github.gdtknight.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.gdtknight.config.JpaConfig;

@Import(JpaConfig.class)
@DataJpaTest
public class ArticleTest {

}
