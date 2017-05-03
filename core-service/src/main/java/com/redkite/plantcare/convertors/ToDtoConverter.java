package com.redkite.plantcare.convertors;


import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface ToDtoConverter<M, T> {

  /**
   * Converts model into Data Transfer object.
   *
   * @param model model to convert
   * @return Data Transfer object
   */
  T toDto(M model);

  /**
   * Converts list of model to list of dto.
   *
   * @param items the list of models
   * @return the list of dto
   */
  default List<T> toDtoList(List<M> items) {
    if (items == null) {
      return null;
    }
    return items.stream().map(this::toDto).collect(Collectors.toList());
  }
}
