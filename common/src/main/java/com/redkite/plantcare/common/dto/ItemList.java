package com.redkite.plantcare.common.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemList<T> {

  private final List<T> items;

  private final Long count;

  public ItemList(List<T> items, Long count) {
    this.items = new ArrayList<>(items);
    this.count = count;
  }
}
