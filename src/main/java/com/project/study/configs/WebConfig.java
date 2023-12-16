package com.project.study.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.study.filters.JwtTokenFilter;
import com.project.study.services.UserService;

@Configuration
@EnableWebSecurity
public class WebConfig {
  private JwtTokenFilter jwtTokenFilter;
  private UserService userService;

  @Autowired
  public WebConfig(JwtTokenFilter jwtTokenFilter, UserService userService) {
    this.jwtTokenFilter = jwtTokenFilter;
    this.userService = userService;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // @Bean
  // public AuthenticationProvider authenticationProvider() {
  // DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
  // authProvider.setUserDetailsService(this.userService);
  // authProvider.setPasswordEncoder(this.passwordEncoder());
  // return authProvider;
  // }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/user/**").hasAuthority("USER")
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
