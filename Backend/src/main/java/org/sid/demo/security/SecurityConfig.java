package org.sid.demo.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .cors(cors -> cors.configurationSource(corsConfigurationSource()))

              .csrf(csrf->csrf.disable())
              .headers(h->h.frameOptions(fo->fo.disable()))
              .authorizeHttpRequests(auth -> {
                 auth.requestMatchers("/",
                                 "/h2-console/**",
                                 "/swagger-ui/**",
                                 "/v3/api-docs/**",
                                 "/v3/api-docs/swagger-config")
                         .permitAll();
                 auth.anyRequest().authenticated();
              })
              .oauth2Login(oauth->oauth.defaultSuccessUrl("http://localhost:8080/student"))
              .formLogin(Customizer.withDefaults());
      return http.build();
  }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
