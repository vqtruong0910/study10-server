package com.project.study.dtos;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  @NotEmpty(message = "Không được để trống email")
  @Email(message = "Email không hợp lệ")
  private String email;

  @NotEmpty(message = "Không được để trống password")
  private String password;

  @NotEmpty(message = "Không được để trống họ và tên")
  private String fullName;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthDate;

  // @Column(unique = true)
  @NotEmpty(message = "Không được để trống số điện thoại")
  private String phoneNumber;

  private String address;

  @NotEmpty(message = "Không được để trống giới tính")
  private int gender;
}
