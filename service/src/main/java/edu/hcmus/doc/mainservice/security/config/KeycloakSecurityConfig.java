package edu.hcmus.doc.mainservice.security.config;

import edu.hcmus.doc.DocURL;
import edu.hcmus.doc.mainservice.security.filter.SecretKeyAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Order(2)
@EnableWebSecurity
public class KeycloakSecurityConfig {

  @Value("${doc.http.auth-token-header-name}")
  private String principalRequestHeader;

  @Value("${doc.http.auth-token}")
  private String principalRequestValue;

  @Bean
  public SecurityFilterChain keycloakSecurityFilterChain(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(secretKeyAuthFilter())
        .antMatcher(DocURL.API_V1 + "/keycloak/users/**")
        .authorizeRequests()
        .anyRequest()
        .authenticated();

    return http.build();
  }

  private SecretKeyAuthFilter secretKeyAuthFilter() {
    SecretKeyAuthFilter secretKeyAuthFilter = new SecretKeyAuthFilter(principalRequestHeader);
    secretKeyAuthFilter.setAuthenticationManager(authentication -> {
      String principal = (String) authentication.getPrincipal();
      if (!principalRequestValue.equals(principal))
      {
        throw new BadCredentialsException("The API key was not found or not the expected value.");
      }
      authentication.setAuthenticated(true);
      return authentication;
    });

    return secretKeyAuthFilter;
  }
}
