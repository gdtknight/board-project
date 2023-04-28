package io.github.gdtknight.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
public class ThymeleafConfig {

  @Bean
  public SpringResourceTemplateResolver thymeleafTemplateResolver(
      SpringResourceTemplateResolver defaultTemplateResolver,
      Thymeleaf3Properties thymeleaf3Properties) {
    defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

    return defaultTemplateResolver;
  }

  @RequiredArgsConstructor
  @ConfigurationProperties("spring.thymeleaf3")
  @ConstructorBinding
  @Getter
  public static class Thymeleaf3Properties {
    /** Use Thymeleaf 3 Decoupled Logic */
    private final boolean decoupledLogic;
  }
}
