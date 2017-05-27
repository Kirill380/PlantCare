package com.redkite.plantcare.controllers.filters;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseFilter implements Pageable {

  protected int offset;

  protected int limit;

  protected String sort;


  @Override
  public int getPageNumber() {
    return 0;
  }

  @Override
  public int getPageSize() {
    return limit == 0 ? 20 : limit;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return new Sort(sort != null ? sort : "email");
  }

  @Override
  public Pageable next() {
    return null;
  }

  @Override
  public Pageable previousOrFirst() {
    return null;
  }

  @Override
  public Pageable first() {
    return null;
  }

  @Override
  public boolean hasPrevious() {
    return false;
  }
}
