package com.redkite.plantcare.controllers.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserFilter extends BaseFilter {

  private String email;

  public UserFilter(String email, int offset, int limit, String sort) {
    super(offset, limit, sort);
    this.email = email;
  }
}
