package com.project.study.entity;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String createBy;

  @Temporal(TemporalType.DATE)
  private Date createDate;

  private String modifiedBy;

  @Temporal(TemporalType.DATE)
  private Date modifiDate;

  public Long getId() {
    return id;
  }

  public String getCreateBy() {
    return createBy;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public Date getModifiDate() {
    return modifiDate;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public void setModifiDate(Date modifiDate) {
    this.modifiDate = modifiDate;
  }
}
