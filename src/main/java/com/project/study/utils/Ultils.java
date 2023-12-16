package com.project.study.utils;

import org.springframework.util.DigestUtils;

public class Ultils {
  public static String encodeMd5(String data) {
    data = data + "study";
    return DigestUtils.md5DigestAsHex(data.getBytes()).toUpperCase();
  }
}
