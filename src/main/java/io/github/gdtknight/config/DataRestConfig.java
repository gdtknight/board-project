package io.github.gdtknight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import io.github.gdtknight.domain.UserAccount;

@Configuration
public class DataRestConfig {

  @Bean
  public RepositoryRestConfigurer repositoryRestConfigurer() {
    return RepositoryRestConfigurer.withConfig(
        (config, cors) -> config.exposeIdsFor(UserAccount.class));
  }
}
