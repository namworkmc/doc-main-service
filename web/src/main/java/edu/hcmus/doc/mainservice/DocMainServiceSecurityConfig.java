package edu.hcmus.doc.mainservice;

import static java.util.Arrays.asList;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.security.filter.SecretKeyAuthFilter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DocMainServiceSecurityConfig {

  private static final String REALM_ACCESS = "realm_access";

  private static final String ROLES = "roles";

  private static final String USERNAME = "username";

  @Value("${doc.http.auth-token-header-name}")
  private String principalRequestHeader;

  @Value("${doc.http.auth-token}")
  private String principalRequestValue;

  @Bean
  @Order(1)
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

  @Bean
  @Order(2)
  public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
    return http.cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(DocURL.API_V1 + "/security/auth/**", "/actuator/**")
        .permitAll()
        .antMatchers(DocURL.API_V1 + "/admin/**")
        .hasAuthority(DocSystemRoleEnum.DOC_ADMIN.value)
        .anyRequest()
        .authenticated()
        .and()
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(false);
    configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
    Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
      Map<String, Collection<String>> realmAccess = jwt.getClaim(REALM_ACCESS);
      Collection<String> roles = realmAccess.get(ROLES);
      return roles.stream()
          .filter(DocSystemRoleEnum.ALL_ROLES_AS_STRING::contains)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());
    };

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    jwtAuthenticationConverter.setPrincipalClaimName(USERNAME);

    return jwtAuthenticationConverter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
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
