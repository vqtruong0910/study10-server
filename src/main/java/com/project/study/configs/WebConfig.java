package com.project.study.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.study.filters.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class WebConfig {
  private JwtTokenFilter jwtTokenFilter;

  @Autowired
  public WebConfig(JwtTokenFilter jwtTokenFilter) {
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    // configuration.addAllowedOrigin("*");
    configuration.addAllowedOrigin("http://localhost:4200");
    configuration.setAllowCredentials(true);
    // configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH",
    // "DELETE"));
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/user/**").hasAuthority("USER")
            .anyRequest().authenticated())
        .addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
