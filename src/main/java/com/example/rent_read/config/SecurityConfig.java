package com.example.rent_read.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    httpSecurity.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
        .requestMatchers(antMatcher(HttpMethod.GET,"/api/books/{id}")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(HttpMethod.GET,"/api/books").hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(HttpMethod.POST,"/api/books").hasAnyRole("ADMIN")
        .requestMatchers(antMatcher(HttpMethod.DELETE,"/api/books/{id}")).hasAnyRole("ADMIN")
        .requestMatchers(antMatcher(HttpMethod.PATCH,"/api/books/{id}")).hasAnyRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/customers").hasAnyRole("ADMIN")
        .requestMatchers(antMatcher("/api/customers/{id}")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(antMatcher("/api/customers/{id}/books")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(antMatcher("/api/books/{id}/rent")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers(antMatcher("/api/books/{id}/return")).hasAnyRole("CUSTOMER", "ADMIN")
        .requestMatchers("/api/login", "/api/register").permitAll());

    httpSecurity.httpBasic(Customizer.withDefaults());

    return httpSecurity.build();
  }
}
