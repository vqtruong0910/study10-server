package com.project.study.utils;

import org.springframework.util.DigestUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class Ultils {
  public static String encodeMd5(String data) {
    data = data + "study";
    return DigestUtils.md5DigestAsHex(data.getBytes()).toUpperCase();
  }

  public static void addRefreshTokenCookie(HttpServletResponse response, String data, int time) {
    Cookie cookie = new Cookie("refresh_token", data);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(time);
    response.addCookie(cookie);
  }
}
