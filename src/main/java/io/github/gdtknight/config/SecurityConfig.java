package io.github.gdtknight.config;

import io.github.gdtknight.dto.UserAccountDto;
import io.github.gdtknight.repository.UserAccountRepository;
import io.github.gdtknight.security.BoardPrincipal;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(auth -> auth
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                    .mvcMatchers(
                            HttpMethod.GET,
                            "/",
                            "/articles",
                            "/articles/search-hashtag"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .formLogin().and()
            .build();
  }

  @Bean
  public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
    return username -> userAccountRepository
            .findById(username)
            .map(UserAccountDto::fromEntity)
            .map(BoardPrincipal::fromDto)
            .orElseThrow(()-> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
