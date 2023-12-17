package com.project.study.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageAlert {
  private int status;
  private String message;
}
