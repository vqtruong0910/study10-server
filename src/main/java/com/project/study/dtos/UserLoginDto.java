package com.project.study.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserLoginDto {
  @NotEmpty(message = "Không được để trống email")
  @Email(message = "Email không hợp lệ")
  private String email;

  @NotEmpty(message = "Không được để trống password")
  private String password;
}
