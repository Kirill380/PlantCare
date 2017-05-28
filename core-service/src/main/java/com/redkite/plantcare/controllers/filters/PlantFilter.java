package com.redkite.plantcare.controllers.filters;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlantFilter extends BaseFilter {

  private String name;

  public PlantFilter(String name, int offset, int limit, String sort) {
    super(offset, limit, sort);
    this.name = name;
  }
}
