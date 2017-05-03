package com.redkite.plantcare.convertors;


import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface ToModelConverter<M, T> {

  /**
   * Converts Data Transfer object into entity.
   *
   * @param dto dto to convert
   * @return entity
   */
  M toModel(T dto);

  /**
   * Converts list of model to list of dto.
   *
   * @param items the list of dto
   * @return the list of models
   */
  default List<M> toModelList(List<T> items) {
    if (items == null) {
      return null;
    }
    return items.stream().map(this::toModel).collect(Collectors.toList());
  }
}
