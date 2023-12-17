package com.project.study.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.study.services.UserService;
import com.project.study.utils.JwtUltils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String jwt = header.substring(7);

    DecodedJWT decodedJWT = JwtUltils.decodeJwt(jwt);
    if (decodedJWT != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      String email = decodedJWT.getClaim("email").asString();
      try {
        UserDetails userDetails = this.userService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
            userDetails.getPassword(),
            userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      } catch (Exception e) {
        log.info("Email " + email + " unauthorized");
      }
    }
    filterChain.doFilter(request, response);
  }

}
