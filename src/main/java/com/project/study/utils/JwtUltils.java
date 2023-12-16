package com.project.study.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUltils {
  private final static String secretKey = "study10-maidinh";

  public static String createAccessJwt(String email, String role) {
    String token = JWT.create()
        .withClaim("email", email)
        .withClaim("role", role)
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 minus
        .sign(Algorithm.HMAC256(secretKey));

    return token;
  }

  public static String createRefreshJwt(String email) {
    String token = JWT.create()
        .withClaim("email", email)
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30)) // 30 day
        .sign(Algorithm.HMAC256(secretKey));

    return token;
  }

  public static boolean verifyJwt(String jwt) {
    try {
      JWT.require(Algorithm.HMAC256(secretKey)).build().verify(jwt);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static DecodedJWT decodeJwt(String jwt) {
    try {
      DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(jwt);
      return decodedJWT;
    } catch (Exception e) {
      return null;
    }
  }
}
