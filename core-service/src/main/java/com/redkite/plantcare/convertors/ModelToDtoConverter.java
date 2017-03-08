package com.redkite.plantcare.convertors;


import java.util.List;
import java.util.stream.Collectors;

public interface ModelToDtoConverter<M, T> {

    T toDto(M model);

    default List<T> toDtoList(List<M> items) {
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
