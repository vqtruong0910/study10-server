package com.project.study.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.study.dtos.Gender;
import com.project.study.dtos.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class UserEntity extends BaseEntity implements UserDetails {
  @Column(unique = true)
  private String email;

  private String password;

  private String fullName;

  @Temporal(TemporalType.DATE)
  private Date birthDate;

  // @Column(unique = true)
  private String phoneNumber;

  private String address;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Enumerated(EnumType.STRING)
  private Role role;

  public UserEntity(String email, String password, String fullName, Date birthDate, String phoneNumber, String address,
      Gender gender, Role role) {
    this.email = email;
    this.password = password;
    this.fullName = fullName;
    this.birthDate = birthDate;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.gender = gender;
    this.role = role;

    super.setCreateBy(fullName);
    super.setCreateDate(new Date(System.currentTimeMillis()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(this.role.name()));
  }

  @Override
  public String getUsername() {
    return this.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
